package com.martdev.android.newsfeed.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.martdev.android.newsfeed.adapter.NewsAdapter
import com.martdev.android.newsfeed.databinding.GenericRecyclerBinding
import com.martdev.android.newsfeed.viewmodel.NewsFeedFactory
import com.martdev.android.newsfeed.viewmodel.NewsFeedVM
import com.newsfeed.shared.DataState
import com.newsfeed.shared.NewsFeedRepo
import com.newsfeed.shared.db.DatabaseDriverFactory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@FlowPreview
class NewsFeedFragment : Fragment() {

    private var binding: GenericRecyclerBinding? = null

    private val viewModel by viewModels<NewsFeedVM> {
        NewsFeedFactory(NewsFeedRepo(DatabaseDriverFactory(requireActivity()).createDriver()))
    }
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GenericRecyclerBinding.inflate(inflater, container, false)

        adapter = NewsAdapter {
            val args = NewsFeedFragmentDirections.actionNewsFeedFragmentToNewsFeedWebView(it)
            findNavController().navigate(args)
        }

        binding?.recyclerView?.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        observeDataState()

        binding?.swipe!!.setOnRefreshListener {
            viewModel.refreshNews(true)
        }
        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
    private fun observeDataState() {
        lifecycleScope.launch {
            viewModel.newsFeedState.collect { dataState ->
                when(dataState) {
                    is DataState.Success -> {
                        setSwipeState(false)
                        binding?.recyclerView?.visibility = View.VISIBLE
                        binding?.recyclerView!!.adapter = adapter
                        adapter.submitList(dataState.data)
                    }
                    is DataState.Error -> {
                        setSwipeState(false)
                        binding?.recyclerView?.visibility = View.GONE
                        binding?.emptyTextMessage?.visibility = View.VISIBLE
                    }
                    DataState.Empty -> {
                        setSwipeState(false)
                        binding?.recyclerView?.visibility = View.GONE
                        binding?.emptyTextMessage?.visibility = View.VISIBLE
                    }
                    DataState.Loading -> {
                        setSwipeState(true)
                        binding?.recyclerView?.visibility = View.GONE
                        binding?.emptyTextMessage?.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setSwipeState(refresh: Boolean) {
        binding?.swipe!!.isRefreshing = refresh
    }
}