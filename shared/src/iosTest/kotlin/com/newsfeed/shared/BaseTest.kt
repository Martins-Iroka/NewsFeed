package com.newsfeed.shared

import co.touchlab.sqliter.DatabaseConfiguration
import com.newsfeed.shared.db.DatabaseDriverFactory
import com.newsfeed.shared.db.NewsFeedDb
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import platform.CoreFoundation.CFRunLoopGetCurrent
import platform.CoreFoundation.CFRunLoopRun
import platform.CoreFoundation.CFRunLoopStop

actual abstract class BaseTest {
    actual fun <T> runTest(block: suspend CoroutineScope.() -> T) {
        var error: Throwable? = null
        GlobalScope.launch(Dispatchers.Main) {
            try {
                block()
            } catch (t: Throwable) {
                error = t
            } finally {
                CFRunLoopStop(CFRunLoopGetCurrent())
            }
        }
        CFRunLoopRun()
        error?.also { throw it }
    }
}

internal actual fun testDbConnection(): SqlDriver {
    val schema = NewsFeedDb.Schema
    return NativeSqliteDriver(
        DatabaseConfiguration(
            name = "NewsFeedDb",
            version = schema.version,
            create = { connection ->
                wrapConnection(connection) { schema.create(it) }
            },
            upgrade = { connection, oldVersion, newVersion ->
                wrapConnection(connection) { schema.migrate(it, oldVersion, newVersion) }
            },
            inMemory = true
        )
    )
}
