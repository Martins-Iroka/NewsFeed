package com.newsfeed.shared

import com.newsfeed.shared.db.DatabaseHelper
import com.newsfeed.shared.remoteSource.NewsApi
import com.squareup.sqldelight.db.SqlDriver
import comnewsfeedshareddb.NewsFeed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull

class NewsFeedRepo(databaseDriverFactory: SqlDriver) {
    private val db = DatabaseHelper(databaseDriverFactory, Dispatchers.Default)
    private val remote = NewsApi()

    fun refreshedNewsFeedIfStale(forced: Boolean = false): Flow<DataState<List<NewsFeed>>> = flow {
        emit(DataState.Loading)
        val remoteNewsFeed: DataState<List<NewsFeed>>
        if (forced) {
            remoteNewsFeed = getNewsFromNetwork()
            when(remoteNewsFeed) {
                DataState.Empty -> {
                    emit(remoteNewsFeed)
                }
                is DataState.Success -> db.insertNewsFeed(remoteNewsFeed.data)
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

    suspend fun getNewsFromNetwork(): DataState<List<NewsFeed>> {
        return try {
            val newsFeed = remote.getTopHeadlines().articles
            if (newsFeed.isEmpty()) {
                DataState.Empty
            } else {
                DataState.Success(
                    newsFeed.map {
                        NewsFeed(0L, it.author?: "", it.title, it.urlToImage, it.url)
                    }
                )
            }
        } catch (e: Exception) {
            DataState.Error("Unable to download newsfeed.\n " +
                    "Also, this maybe the reason  ${e.message}")
        }
    }

    fun getNewsById(id: Long): Flow<DataState<NewsFeed>> =
        db.selectById(id)
            .mapNotNull { news ->
                DataState.Success(news)
            }
}