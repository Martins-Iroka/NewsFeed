package com.newsfeed.shared.db

import com.newsfeed.shared.db.shared.newInstance
import com.newsfeed.shared.db.shared.schema
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver
import comnewsfeedshareddb.NewsFeedQueries

public interface NewsFeedDb : Transacter {
  public val newsFeedQueries: NewsFeedQueries

  public companion object {
    public val Schema: SqlDriver.Schema
      get() = NewsFeedDb::class.schema

    public operator fun invoke(driver: SqlDriver): NewsFeedDb =
        NewsFeedDb::class.newInstance(driver)
  }
}
