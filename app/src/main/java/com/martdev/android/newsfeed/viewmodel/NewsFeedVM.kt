package com.martdev.android.newsfeed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.newsfeed.shared.DataState
import com.newsfeed.shared.NewsFeedRepo
import comnewsfeedshareddb.NewsFeed
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@FlowPreview
class NewsFeedVM(private val repo: NewsFeedRepo) : ViewModel() {

   private val _newsFeedState = MutableStateFlow<DataState<List<NewsFeed>>>(DataState.Loading)

    val newsFeedState: StateFlow<DataState<List<NewsFeed>>> = _newsFeedState

    init {
        getNewsFeed(true)
    }

    fun getNewsFeed(refresh: Boolean) {
        viewModelScope.launch {
            flowOf(
                repo.refreshedNewsFeed(refresh),
                repo.getNewsFromCache()
            ).flattenMerge().collect { dataState ->
                    _newsFeedState.value = dataState
                }
        }
    }

    fun refreshNews(refresh: Boolean) {
        viewModelScope.launch {
            getNewsFeed(refresh)
        }
    }
}

class NewsFeedFactory(private val repo: NewsFeedRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsFeedVM(repo) as T
    }
}