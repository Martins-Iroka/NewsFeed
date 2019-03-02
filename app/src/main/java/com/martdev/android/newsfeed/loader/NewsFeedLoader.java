package com.martdev.android.newsfeed.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.martdev.android.newsfeed.model.NewsInfo;
import com.martdev.android.newsfeed.utils.QueryUtils;

import java.util.List;

public class NewsFeedLoader extends AsyncTaskLoader<List<NewsInfo>> {

    private String mUrl;

    public NewsFeedLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Nullable
    @Override
    public List<NewsInfo> loadInBackground() {
        if (mUrl == null)
            return null;

        return QueryUtils.getNewsData(mUrl);
    }
}
