package com.newsfeed.shared.db.shared

import com.newsfeed.shared.db.NewsFeedDb
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.`internal`.copyOnWriteList
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import comnewsfeedshareddb.NewsFeed
import comnewsfeedshareddb.NewsFeedQueries
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList
import kotlin.jvm.JvmField
import kotlin.reflect.KClass

internal val KClass<NewsFeedDb>.schema: SqlDriver.Schema
  get() = NewsFeedDbImpl.Schema

internal fun KClass<NewsFeedDb>.newInstance(driver: SqlDriver): NewsFeedDb = NewsFeedDbImpl(driver)

private class NewsFeedDbImpl(
  driver: SqlDriver
) : TransacterImpl(driver), NewsFeedDb {
  public override val newsFeedQueries: NewsFeedQueriesImpl = NewsFeedQueriesImpl(this, driver)

  public object Schema : SqlDriver.Schema {
    public override val version: Int
      get() = 1

    public override fun create(driver: SqlDriver): Unit {
      driver.execute(null, """
          |CREATE TABLE NewsFeed (
          |id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |author TEXT NOT NULL,
          |title TEXT NOT NULL,
          |urlToImage TEXT,
          |url TEXT NOT NULL
          |)
          """.trimMargin(), 0)
    }

    public override fun migrate(
      driver: SqlDriver,
      oldVersion: Int,
      newVersion: Int
    ): Unit {
    }
  }
}

private class NewsFeedQueriesImpl(
  private val database: NewsFeedDbImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), NewsFeedQueries {
  internal val selectAll: MutableList<Query<*>> = copyOnWriteList()

  internal val selectById: MutableList<Query<*>> = copyOnWriteList()

  public override fun <T : Any> selectAll(mapper: (
    id: Long,
    author: String,
    title: String,
    urlToImage: String?,
    url: String
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

  public override fun selectAll(): Query<NewsFeed> = selectAll { id, author, title, urlToImage,
      url ->
    NewsFeed(
      id,
      author,
      title,
      urlToImage,
      url
    )
  }

  public override fun <T : Any> selectById(id: Long, mapper: (
    id: Long,
    author: String,
    title: String,
    urlToImage: String?,
    url: String
  ) -> T): Query<T> = SelectByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3),
      cursor.getString(4)!!
    )
  }

  public override fun selectById(id: Long): Query<NewsFeed> = selectById(id) { id_, author, title,
      urlToImage, url ->
    NewsFeed(
      id_,
      author,
      title,
      urlToImage,
      url
    )
  }

  public override fun insertNewsFeed(
    id: Long?,
    author: String,
    title: String,
    urlToImage: String?,
    url: String
  ): Unit {
    driver.execute(1325564302, """
    |INSERT OR IGNORE INTO NewsFeed(id, author, title, urlToImage, url)
    |VALUES (?,?,?,?,?)
    """.trimMargin(), 5) {
      bindLong(1, id)
      bindString(2, author)
      bindString(3, title)
      bindString(4, urlToImage)
      bindString(5, url)
    }
    notifyQueries(1325564302, {database.newsFeedQueries.selectAll +
        database.newsFeedQueries.selectById})
  }

  public override fun deleteAll(): Unit {
    driver.execute(659382482, """DELETE FROM NewsFeed""", 0)
    notifyQueries(659382482, {database.newsFeedQueries.selectAll +
        database.newsFeedQueries.selectById})
  }

  private inner class SelectByIdQuery<out T : Any>(
    @JvmField
    public val id: Long,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectById, mapper) {
    public override fun execute(): SqlCursor = driver.executeQuery(-159516270,
        """SELECT * FROM NewsFeed WHERE id = ?""", 1) {
      bindLong(1, id)
    }

    public override fun toString(): String = "NewsFeed.sq:selectById"
  }
}
