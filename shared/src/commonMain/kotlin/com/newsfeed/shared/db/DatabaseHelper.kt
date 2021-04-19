package com.newsfeed.shared.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import comnewsfeedshareddb.NewsFeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class DatabaseHelper(
    sqlDriver: SqlDriver,
    private val backgroundDispatcher: CoroutineDispatcher
) {

    private val dbRef = NewsFeedDb(sqlDriver)

    fun selectAllItems(): Flow<List<NewsFeed>> =
        dbRef.newsFeedQueries
            .selectAll()
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    fun selectById(id: Long): Flow<NewsFeed> =
        dbRef.newsFeedQueries
            .selectById(id)
            .asFlow()
            .mapToOne()
            .flowOn(backgroundDispatcher)

    suspend fun insertNewsFeed(breeds: List<NewsFeed>) {
        dbRef.transactionWithContext(backgroundDispatcher) {
            breeds.forEach { news ->
                dbRef.newsFeedQueries
                    .insertNewsFeed(null, news.author, news.title, news.urlToImage, news.content)
            }
        }
    }

    suspend fun deleteAllNewsFeed() {
        dbRef.transactionWithContext(backgroundDispatcher) {
            dbRef.newsFeedQueries.deleteAll()
        }
    }
}