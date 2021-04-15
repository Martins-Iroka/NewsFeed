package com.martdev.android.newsfeed.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.martdev.android.newsfeed.R;
import com.martdev.android.newsfeed.fragment.NewsFeedPreference;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager manager = getFragmentManager();
        PreferenceFragment fragment = (PreferenceFragment) manager.findFragmentById(R.id.settings_fragment_container);
        if (fragment == null) {
            fragment = new NewsFeedPreference();
            manager.beginTransaction().add(R.id.settings_fragment_container, fragment).commit();
        }
    }
}
