package com.newsfeed.shared

import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.CoroutineScope

expect abstract class BaseTest() {
    fun <T> runTest(block: suspend CoroutineScope.() -> T)
}

internal expect fun testDbConnection(): SqlDriver