package com.newsfeed.shared

expect class Platform() {
    val platform: String
}

internal expect fun printThrowable(t: Throwable)