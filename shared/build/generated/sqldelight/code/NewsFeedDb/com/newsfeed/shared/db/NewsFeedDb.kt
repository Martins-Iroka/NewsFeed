package com.newsfeed.shared.db

import com.newsfeed.shared.db.shared.newInstance
import com.newsfeed.shared.db.shared.schema
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver
import comnewsfeedshareddb.NewsFeedQueries

interface NewsFeedDb : Transacter {
  val newsFeedQueries: NewsFeedQueries

  companion object {
    val Schema: SqlDriver.Schema
      get() = NewsFeedDb::class.schema

    operator fun invoke(driver: SqlDriver): NewsFeedDb = NewsFeedDb::class.newInstance(driver)}
}
