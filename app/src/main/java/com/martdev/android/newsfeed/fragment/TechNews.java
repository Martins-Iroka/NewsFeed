package com.martdev.android.newsfeed.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martdev.android.newsfeed.R;
import com.martdev.android.newsfeed.adapter.NewsAdapter;
import com.martdev.android.newsfeed.loader.NewsFeedLoader;
import com.martdev.android.newsfeed.model.NewsInfo;

import java.util.ArrayList;
import java.util.List;

public class TechNews extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsInfo>> {

    private static final String NEWS_URL = "https://newsapi.org/v2/top-headlines";
    private static final String NEWS_API = "8bd6245749de492ba64956f4132143db";

    private static final int NEWSFEED_LOADER_ID = 6;

    private NewsAdapter mNewsAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generic_recycler, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRefreshLayout = view.findViewById(R.id.swipe);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkNetwork();
            }
        });
        mRefreshLayout.setRefreshing(true);

        mEmptyTextView = view.findViewById(R.id.empty_text_message);

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

            if (loaderManager.getLoader(NEWSFEED_LOADER_ID) == null) {
                loaderManager.initLoader(NEWSFEED_LOADER_ID, null, TechNews.this);
            } else {
                loaderManager.restartLoader(NEWSFEED_LOADER_ID, null, TechNews.this);
            }
        } else {
            mRefreshLayout.setRefreshing(false);

            mEmptyTextView.setText(R.string.no_internet);
        }
    }

    @NonNull
    @Override
    public Loader<List<NewsInfo>> onCreateLoader(int id, @Nullable Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String country = sharedPreferences.getString(
                getString(R.string.news_location_key),
                getString(R.string.news_location_default_value)
        );

        Uri baseUri = Uri.parse(NEWS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("country", country);
        uriBuilder.appendQueryParameter("category", "technology");
        uriBuilder.appendQueryParameter("pageSize", "50");
        uriBuilder.appendQueryParameter("apiKey", NEWS_API);
        return new NewsFeedLoader(getActivity(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsInfo>> loader, List<NewsInfo> data) {
        mRefreshLayout.setRefreshing(false);

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
