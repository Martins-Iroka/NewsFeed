package comnewsfeedshareddb

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String
import kotlin.Unit

public interface NewsFeedQueries : Transacter {
  public fun <T : Any> selectAll(mapper: (
    id: Long,
    author: String,
    title: String,
    urlToImage: String?,
    url: String
  ) -> T): Query<T>

  public fun selectAll(): Query<NewsFeed>

  public fun <T : Any> selectById(id: Long, mapper: (
    id: Long,
    author: String,
    title: String,
    urlToImage: String?,
    url: String
  ) -> T): Query<T>

  public fun selectById(id: Long): Query<NewsFeed>

  public fun insertNewsFeed(
    id: Long?,
    author: String,
    title: String,
    urlToImage: String?,
    url: String
  ): Unit

  public fun deleteAll(): Unit
}
