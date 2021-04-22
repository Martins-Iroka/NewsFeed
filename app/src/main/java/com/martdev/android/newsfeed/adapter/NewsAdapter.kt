package com.martdev.android.newsfeed.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat.IntentBuilder;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.martdev.android.newsfeed.R;
import com.martdev.android.newsfeed.activity.NewsPageActivity;
import com.martdev.android.newsfeed.model.NewsInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private Activity mActivity;
    private List<NewsInfo> mNewsInfoList;

    static class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private NewsInfo mInfo;
        private ImageView mImageView, mShareUrl;
        private TextView mTitle, mSource, mPublishedDate;
        private Activity mActivity;

        NewsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.news_item_view, parent, false));
            itemView.setOnClickListener(this);

            mImageView = itemView.findViewById(R.id.news_image);

            mTitle = itemView.findViewById(R.id.news_title);
            mSource = itemView.findViewById(R.id.news_source);
            mPublishedDate = itemView.findViewById(R.id.published_date);

            mShareUrl = itemView.findViewById(R.id.share_url);
            mShareUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = IntentBuilder.from(mActivity).setType("text/plain")
                            .setText(mInfo.getURL())
                            .setChooserTitle(R.string.send_link)
                            .createChooserIntent();
                    itemView.getContext().startActivity(i);
                }
            });
        }

        private void bind(NewsInfo info, Activity activity) {
            mInfo = info;
            mActivity = activity;
            Uri uri = Uri.parse(mInfo.getImageURL());
            Picasso.with(activity).load(uri).placeholder(R.drawable.images).fit().centerCrop().into(mImageView);

            mTitle.setText(mInfo.getTitle());
            mSource.setText(mInfo.getSource());
            mPublishedDate.setText(mInfo.getPublishedDate());
        }

        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse(mInfo.getURL());

            Intent intent = NewsPageActivity.newIntent(itemView.getContext(), uri);
            itemView.getContext().startActivity(intent);
        }
    }

    public NewsAdapter(Activity activity, List<NewsInfo> infoList) {
        mActivity = activity;
        mNewsInfoList = infoList;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        return new NewsHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsHolder holder, int position) {
        final NewsInfo newsInfo = mNewsInfoList.get(position);
        holder.bind(newsInfo, mActivity);
    }

    @Override
    public int getItemCount() {
        return mNewsInfoList.size();
    }
}
