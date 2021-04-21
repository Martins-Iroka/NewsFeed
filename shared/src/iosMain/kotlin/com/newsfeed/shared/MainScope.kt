package com.newsfeed.shared

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class MainScope(private val mainContext: CoroutineContext) :
    CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = mainContext + job + exceptionHandler

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        printThrowable(throwable)
        showError(throwable)
    }
    
    private fun showError(t: Throwable) {
        println(t)
    }

    fun onDestroy() {
        job.cancel()
    }
}
