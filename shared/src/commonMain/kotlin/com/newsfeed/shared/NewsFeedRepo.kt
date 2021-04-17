package com.newsfeed.shared

import com.newsfeed.shared.db.DatabaseDriverFactory
import com.newsfeed.shared.db.DatabaseHelper
import com.newsfeed.shared.remoteSource.NewsApi
import com.russhwolf.settings.Settings
import comnewsfeedshareddb.NewsFeed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.datetime.Clock

class NewsFeedRepo(databaseDriverFactory: DatabaseDriverFactory) {
    private val db = DatabaseHelper(databaseDriverFactory, Dispatchers.Default)
    private val remote = NewsApi()
    private val settings = Settings()
    private val clock = Clock.System

    companion object {
        internal const val DB_TIMESTAMP_KEY = "DbTimestampKey"

    }

    fun refreshedNewsFeedIfStale(forced: Boolean = false): Flow<DataState<List<NewsFeed>>> = flow {
        emit(DataState.Loading)
        val currentTimeMS = clock.now().toEpochMilliseconds()
        val stale = isNewsFeedListStale(currentTimeMS)
        val remoteNewsFeed: DataState<List<NewsFeed>>
        if (stale || forced) {
            remoteNewsFeed = getNewsFromNetwork(currentTimeMS)
            when(remoteNewsFeed) {
                DataState.Empty -> {
                    emit(remoteNewsFeed)
                }
                is DataState.Success -> db.insertBreeds(remoteNewsFeed.data)
                is DataState.Error -> emit(remoteNewsFeed)
                DataState.Loading -> {}
            }
        }
    }
    fun getNewsFromCache(): Flow<DataState<List<NewsFeed>>> =
        db.selectAllItems()
            .mapNotNull { newsList ->
                if (newsList.isEmpty()) {
                    null
                } else {
                    DataState.Success(newsList)
                }
            }

    suspend fun getNewsFromNetwork(currentTimeMS: Long): DataState<List<NewsFeed>> {
        return try {
            val newsFeed = remote.getTopHeadlines().articles
            settings.putLong(DB_TIMESTAMP_KEY, currentTimeMS)
            if (newsFeed.isEmpty()) {
                DataState.Empty
            } else {
                DataState.Success(
                    newsFeed.map {
                        NewsFeed(0L, it.author?: "", it.title, it.urlToImage, it.content)
                    }
                )
            }
        } catch (e: Exception) {
            DataState.Error("Unable to download newsfeed.\n " +
                    "Also, this maybe the reason  ${e.message}")
        }
    }
    private fun isNewsFeedListStale(currentTimeMS: Long): Boolean {
        val lastDownloadTimeMS = settings.getLong(DB_TIMESTAMP_KEY, 0)
        val oneHourMS = 60 * 60 * 1000

        return lastDownloadTimeMS + oneHourMS < currentTimeMS
    }
}