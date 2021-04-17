package comnewsfeedshareddb

import kotlin.Long
import kotlin.String

data class NewsFeed(
  val id: Long,
  val author: String,
  val title: String,
  val urlToImage: String?,
  val content: String
) {
  override fun toString(): String = """
  |NewsFeed [
  |  id: $id
  |  author: $author
  |  title: $title
  |  urlToImage: $urlToImage
  |  content: $content
  |]
  """.trimMargin()
}
