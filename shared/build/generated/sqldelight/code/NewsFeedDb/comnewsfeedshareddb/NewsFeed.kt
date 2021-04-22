package comnewsfeedshareddb

import kotlin.Long
import kotlin.String

public data class NewsFeed(
  public val id: Long,
  public val author: String,
  public val title: String,
  public val urlToImage: String?,
  public val url: String
) {
  public override fun toString(): String = """
  |NewsFeed [
  |  id: $id
  |  author: $author
  |  title: $title
  |  urlToImage: $urlToImage
  |  url: $url
  |]
  """.trimMargin()
}
