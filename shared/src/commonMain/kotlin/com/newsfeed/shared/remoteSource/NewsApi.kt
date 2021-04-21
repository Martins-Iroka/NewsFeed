package com.newsfeed.shared.remoteSource

import com.newsfeed.shared.model.NewsInfo
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*

class NewsApi {

    private val client = HttpClient {
        install(JsonFeature) {
            val json = kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            }
            serializer = KotlinxSerializer(json)
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    suspend fun getTopHeadlines() = client.get<NewsInfo>(
        "https://newsapi.org/v2/top-headlines?country=us&apiKey=8bd6245749de492ba64956f4132143db"
    )

}