package com.martdev.android.newsfeed.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.webkit.WebView;

import com.martdev.android.newsfeed.R;
import com.martdev.android.newsfeed.fragment.NewsPage;

public class NewsPageActivity extends AppCompatActivity {

    public static Intent newIntent(Context context, Uri newsPageUri) {
        Intent intent = new Intent(context, NewsPageActivity.class);
        intent.setData(newsPageUri);
        return intent;
    }

    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Uri uri = getIntent().getData();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = NewsPage.newInstance(uri);
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        mWebView = findViewById(R.id.web_view);
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
