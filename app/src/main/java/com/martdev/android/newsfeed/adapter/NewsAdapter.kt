package com.martdev.android.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martdev.android.newsfeed.R
import com.martdev.android.newsfeed.databinding.NewsItemViewBinding
import com.squareup.picasso.Picasso
import comnewsfeedshareddb.NewsFeed


class NewsAdapter(
    private val onNewsFeedClicked: (String) -> Unit
) : ListAdapter<NewsFeed, NewsViewHolder>(newsFeedCallback){

    companion object {
        private val newsFeedCallback = object : DiffUtil.ItemCallback<NewsFeed>() {
            override fun areContentsTheSame(oldItem: NewsFeed, newItem: NewsFeed): Boolean = oldItem == newItem

            override fun areItemsTheSame(oldItem: NewsFeed, newItem: NewsFeed): Boolean = oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsFeed = getItem(position)
        holder.bind(newsFeed)
        holder.itemView.setOnClickListener {
            onNewsFeedClicked(newsFeed.url)
        }
    }
}

class NewsViewHolder(private val binding: NewsItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(newsFeed: NewsFeed) {
        binding.newsAuthor.text = if (newsFeed.author.isEmpty()) {
            binding.root.context.getString(R.string.no_author_message)
        } else {
            newsFeed.author
        }
        Picasso.get().load(newsFeed.urlToImage)
            .error(R.drawable.images)
            .into(binding.newsImage)
        binding.newsTitle.text = newsFeed.title
    }

    companion object {

    }
}