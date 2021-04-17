package comnewsfeedshareddb

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String

interface NewsFeedQueries : Transacter {
  fun <T : Any> selectAll(mapper: (
    id: Long,
    author: String,
    title: String,
    urlToImage: String?,
    content: String
  ) -> T): Query<T>

  fun selectAll(): Query<NewsFeed>

  fun <T : Any> selectById(id: Long, mapper: (
    id: Long,
    author: String,
    title: String,
    urlToImage: String?,
    content: String
  ) -> T): Query<T>

  fun selectById(id: Long): Query<NewsFeed>

  fun insertBreed(
    id: Long?,
    author: String,
    title: String,
    urlToImage: String?,
    content: String
  )
}
