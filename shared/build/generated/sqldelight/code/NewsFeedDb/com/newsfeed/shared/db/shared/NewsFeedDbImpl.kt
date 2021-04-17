package com.newsfeed.shared.db.shared

import com.newsfeed.shared.db.NewsFeedDb
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.internal.copyOnWriteList
import comnewsfeedshareddb.NewsFeed
import comnewsfeedshareddb.NewsFeedQueries
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.MutableList
import kotlin.jvm.JvmField
import kotlin.reflect.KClass

internal val KClass<NewsFeedDb>.schema: SqlDriver.Schema
  get() = NewsFeedDbImpl.Schema

internal fun KClass<NewsFeedDb>.newInstance(driver: SqlDriver): NewsFeedDb = NewsFeedDbImpl(driver)

private class NewsFeedDbImpl(
  driver: SqlDriver
) : TransacterImpl(driver), NewsFeedDb {
  override val newsFeedQueries: NewsFeedQueriesImpl = NewsFeedQueriesImpl(this, driver)

  object Schema : SqlDriver.Schema {
    override val version: Int
      get() = 1

    override fun create(driver: SqlDriver) {
      driver.execute(null, """
          |CREATE TABLE NewsFeed (
          |id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |author TEXT NOT NULL,
          |title TEXT NOT NULL,
          |urlToImage TEXT,
          |content TEXT NOT NULL
          |)
          """.trimMargin(), 0)
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Int,
      newVersion: Int
    ) {
    }
  }
}

private class NewsFeedQueriesImpl(
  private val database: NewsFeedDbImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), NewsFeedQueries {
  internal val selectAll: MutableList<Query<*>> = copyOnWriteList()

  internal val selectById: MutableList<Query<*>> = copyOnWriteList()

  override fun <T : Any> selectAll(mapper: (
    id: Long,
    author: String,
    title: String,
    urlToImage: String?,
    content: String
  ) -> T): Query<T> = Query(-697883679, selectAll, driver, "NewsFeed.sq", "selectAll",
      "SELECT * FROM NewsFeed") { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3),
      cursor.getString(4)!!
    )
  }

  override fun selectAll(): Query<NewsFeed> = selectAll { id, author, title, urlToImage, content ->
    NewsFeed(
      id,
      author,
      title,
      urlToImage,
      content
    )
  }

  override fun <T : Any> selectById(id: Long, mapper: (
    id: Long,
    author: String,
    title: String,
    urlToImage: String?,
    content: String
  ) -> T): Query<T> = SelectByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3),
      cursor.getString(4)!!
    )
  }

  override fun selectById(id: Long): Query<NewsFeed> = selectById(id) { id_, author, title,
      urlToImage, content ->
    NewsFeed(
      id_,
      author,
      title,
      urlToImage,
      content
    )
  }

  override fun insertBreed(
    id: Long?,
    author: String,
    title: String,
    urlToImage: String?,
    content: String
  ) {
    driver.execute(-968821737, """
    |INSERT OR IGNORE INTO NewsFeed(id, author, title, urlToImage, content)
    |VALUES (?,?,?,?,?)
    """.trimMargin(), 5) {
      bindLong(1, id)
      bindString(2, author)
      bindString(3, title)
      bindString(4, urlToImage)
      bindString(5, content)
    }
    notifyQueries(-968821737, {database.newsFeedQueries.selectAll +
        database.newsFeedQueries.selectById})
  }

  private inner class SelectByIdQuery<out T : Any>(
    @JvmField
    val id: Long,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectById, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(-159516270,
        """SELECT * FROM NewsFeed WHERE id = ?""", 1) {
      bindLong(1, id)
    }

    override fun toString(): String = "NewsFeed.sq:selectById"
  }
}
