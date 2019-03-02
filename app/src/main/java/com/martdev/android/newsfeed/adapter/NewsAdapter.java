package com.martdev.android.newsfeed.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.martdev.android.newsfeed.R;
import com.martdev.android.newsfeed.model.NewsInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private Context mContext;
    private List<NewsInfo> mNewsInfoList;

    public static class NewsHolder extends RecyclerView.ViewHolder {
        private NewsInfo mInfo;
        private ImageView mImageView;
        private TextView mAuthor, mTitle, mSource, mPublishedDate;

        NewsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.news_item_view, parent, false));

            mImageView = itemView.findViewById(R.id.news_image);

            mAuthor = itemView.findViewById(R.id.news_author);
            mTitle = itemView.findViewById(R.id.news_title);
            mSource = itemView.findViewById(R.id.news_source);
            mPublishedDate = itemView.findViewById(R.id.published_date);
        }

        private void bind(NewsInfo info, Context context) {
            mInfo = info;
            Uri uri = Uri.parse(mInfo.getImageURL());
            Picasso.with(context).load(uri).placeholder(R.drawable.images).resize(150, 100).into(mImageView);

            mAuthor.setText(mInfo.getAuthor());
            mTitle.setText(mInfo.getTitle());
            mSource.setText(mInfo.getSource());
            mPublishedDate.setText(mInfo.getPublishedDate());
        }
    }

    public NewsAdapter(Context context, List<NewsInfo> infoList) {
        mContext = context;
        mNewsInfoList = infoList;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new NewsHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        NewsInfo newsInfo = mNewsInfoList.get(position);
        holder.bind(newsInfo, mContext);
    }

    @Override
    public int getItemCount() {
        return mNewsInfoList.size();
    }
}
