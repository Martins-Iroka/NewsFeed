package com.newsfeed.shared.db

import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(NewsFeedDb.Schema, "test.db")
    }
}