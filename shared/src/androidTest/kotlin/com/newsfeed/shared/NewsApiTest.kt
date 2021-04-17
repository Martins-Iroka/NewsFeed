package com.newsfeed.shared

import com.newsfeed.shared.remoteSource.NewsApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class NewsApiTest {

    private lateinit var api: NewsApi

    @Before
    fun setup() {
        api = NewsApi()
    }

    @Test
    fun getHeadlines() = runBlocking {
        val response = api.getTopHeadlines()
        println(response)
    }
}