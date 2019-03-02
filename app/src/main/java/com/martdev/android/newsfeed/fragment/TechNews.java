package com.martdev.android.newsfeed.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.martdev.android.newsfeed.R;
import com.martdev.android.newsfeed.adapter.NewsAdapter;
import com.martdev.android.newsfeed.loader.NewsFeedLoader;
import com.martdev.android.newsfeed.model.NewsInfo;

import java.util.ArrayList;
import java.util.List;

public class TechNews extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsInfo>> {

    private static final String NEWS_URL = "https://newsapi.org/v2/top-headlines?" +
            "country=ng&category=technology&apiKey=8bd6245749de492ba64956f4132143db";

    private static final int NEWSFEED_LOADER_ID = 6;

    private NewsAdapter mNewsAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generic_recycler, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mEmptyTextView = view.findViewById(R.id.empty_text_message);

        mProgressBar = view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        updateView();
        checkNetwork();

        return view;
    }

    private void updateView() {
        mNewsAdapter = new NewsAdapter(getActivity(), new ArrayList<NewsInfo>());
        mRecyclerView.setAdapter(mNewsAdapter);
    }

    private void checkNetwork() {
        ConnectivityManager manager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            mEmptyTextView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);

            if (loaderManager.getLoader(NEWSFEED_LOADER_ID) == null) {
                loaderManager.initLoader(NEWSFEED_LOADER_ID, null, TechNews.this);
            } else {
                loaderManager.restartLoader(NEWSFEED_LOADER_ID, null, TechNews.this);
            }
        } else {
            mProgressBar.setVisibility(View.GONE);

            mEmptyTextView.setText(R.string.no_internet);
        }
    }

    @NonNull
    @Override
    public Loader<List<NewsInfo>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsFeedLoader(getActivity(), NEWS_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsInfo>> loader, List<NewsInfo> data) {
        mProgressBar.setVisibility(View.GONE);

        mEmptyTextView.setText(R.string.no_news);

        if (data != null && !data.isEmpty()) {
            mNewsAdapter = new NewsAdapter(getActivity(), data);
            mNewsAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mNewsAdapter);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsInfo>> loader) {
    }
}
