package com.newsfeed.shared

import kotlinx.coroutines.flow.*
import kotlin.test.*

class NewsFeedRepoTest : BaseTest() {

    private lateinit var newsFeedRepo: NewsFeedRepo

    private val dbConnection = testDbConnection()
    @BeforeTest
    fun setup() {
        newsFeedRepo = NewsFeedRepo(dbConnection)
    }

    @AfterTest
    fun tearDown() {
        dbConnection.close()
    }
    @Test
    fun getNewsFromRemote() {
        runTest {
            val response = newsFeedRepo.getNewsFromNetwork()
            assertTrue(response is DataState.Success)
        }
    }


    @Test
    fun refreshedDataBase() {
        runTest {
           newsFeedRepo.refreshedNewsFeedIfStale(true).collectLatest {
               when(it) {
                   is DataState.Success -> {
                       val v = newsFeedRepo.getNewsFromCache().first()
                       assertTrue(v is DataState.Success)
                   }
               }
           }
        }
    }
}