package com.newsfeed.shared.model


import kotlinx.serialization.Serializable

@Serializable
data class NewsInfo(
    val status: String,
    val articles: List<Articles>
)

@Serializable
data class Articles(
    val author: String?,
    val title: String,
    val urlToImage: String? = null,
    val content: String? = null
)