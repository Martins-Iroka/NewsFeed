package com.newsfeed.shared

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

internal actual fun printThrowable(t: Throwable) {
    t.printStackTrace()
}