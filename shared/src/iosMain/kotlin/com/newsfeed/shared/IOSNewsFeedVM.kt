package com.newsfeed.shared

import com.newsfeed.shared.db.DatabaseDriverFactory
import comnewsfeedshareddb.NewsFeed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class IOSNewsFeedVM {

    private val scope = MainScope(Dispatchers.Main)
    private val sqlDriver = DatabaseDriverFactory().createDriver()
    private val newsFeedRepo = NewsFeedRepo(sqlDriver)
    private val _newsFeedStateFlow = MutableStateFlow<DataState<List<NewsFeed>>>(
        DataState.Loading
    )

    @FlowPreview
    @Throws(Exception::class)
    fun getNewsFeed(load: Boolean,
                    onLoading: () -> Unit,
                    onSuccess: (List<NewsFeed>) -> Unit,
                    onError: (String) -> Unit,
                    onEmpty: () -> Unit
    ) {

        scope.launch {
            println("getNewsFeed called")
            flowOf(
                newsFeedRepo.refreshedNewsFeed(),
                newsFeedRepo.getNewsFromCache()
            ).flattenMerge().collect { dataState ->
                when(dataState) {
                    is DataState.Success -> {
                        println("getNewsFeed: onSuccess")
                        onSuccess(dataState.data)
                    }
                    is DataState.Error -> {
                        println("getNewsFeed: onError")
                        onError(dataState.exception)
                    }
                    DataState.Empty -> {
                        println("getNewsFeed: onEmpty")
                        onEmpty()
                    }
                    DataState.Loading -> {
                        println("getNewsFeed: onLoading")
                        onLoading()
                    }
                }
            }
        }
    }

    fun onDestroy() {
        scope.onDestroy()
    }
}