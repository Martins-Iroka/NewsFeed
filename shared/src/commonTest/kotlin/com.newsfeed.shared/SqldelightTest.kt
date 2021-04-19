package com.newsfeed.shared

import com.newsfeed.shared.db.DatabaseHelper
import comnewsfeedshareddb.NewsFeed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class SqldelightTest : BaseTest() {

    private lateinit var dbHelper: DatabaseHelper

    private val newsFeeds = listOf(
        NewsFeed(id = 1, author = "Ik1", title = "Title1", urlToImage = "Image 1", content = "Content1"),
        NewsFeed(id = 2, author = "Ik2", title = "Title2", urlToImage = "Image 2", content = "Content2"),
        NewsFeed(id = 3, author = "Ik3", title = "Title3", urlToImage = "Image 3", content = "Content3")
    )
    @BeforeTest
    fun setup() = runTest {
        dbHelper = DatabaseHelper(
            testDbConnection(),
            Dispatchers.Default
        )

        dbHelper.deleteAllNewsFeed()
        dbHelper.insertNewsFeed(
            newsFeeds
        )
    }

    @Test
    fun selectAllNewsFeedSuccess() = runTest {
        val newsFeed = dbHelper.selectAllItems().first()
        assertNotNull(newsFeed.find { it.author == "Ik1" }, "Could not get news")
    }

    @Test
    fun selectNewsFeedByID() = runTest {
        val newsFeed = dbHelper.selectAllItems().first()[2]
        assertNotNull(dbHelper.selectById(newsFeed.id))
    }
}