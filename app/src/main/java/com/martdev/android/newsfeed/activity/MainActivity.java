package com.martdev.android.newsfeed.activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;

import com.martdev.android.newsfeed.R;
import com.martdev.android.newsfeed.fragment.BusinessNews;
import com.martdev.android.newsfeed.fragment.EntertainmentNews;
import com.martdev.android.newsfeed.fragment.HealthNews;
import com.martdev.android.newsfeed.fragment.ScienceNews;
import com.martdev.android.newsfeed.fragment.SportNews;
import com.martdev.android.newsfeed.fragment.TechNews;
import com.martdev.android.newsfeed.fragment.TopHeadlines;
import com.newsfeed.shared.Greeting;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SUBTITLE = "subtitle";
    private DrawerLayout mDrawerLayout;
    private String mSubTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            mSubTitle = savedInstanceState.getString(SUBTITLE);
            getSupportActionBar().setSubtitle(mSubTitle);
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.nav_open_drawer, R.string.nav_close_drawer);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new TopHeadlines();
            getSupportActionBar().setSubtitle(R.string.top_headline);
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        Log.i("Login Activity", "Hello from shared module: " + (new Greeting().greeting()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SUBTITLE, getSupportActionBar().getSubtitle().toString());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = null;
        Intent intent = null;

        switch (id) {
            case R.id.category_business:
                fragment = new BusinessNews();
                mSubTitle = getString(R.string.category_business);
                getSupportActionBar().setSubtitle(mSubTitle);
                break;
            case R.id.category_entertainment:
                fragment = new EntertainmentNews();
                mSubTitle = getString(R.string.category_entertainment);
                getSupportActionBar().setSubtitle(mSubTitle);
                break;
            case R.id.category_health:
                fragment = new HealthNews();
                mSubTitle = getString(R.string.category_health);
                getSupportActionBar().setSubtitle(mSubTitle);
                break;
            case R.id.category_science:
                fragment = new ScienceNews();
                mSubTitle = getString(R.string.category_science);
                getSupportActionBar().setSubtitle(mSubTitle);
                break;
            case R.id.category_sport:
                fragment = new SportNews();
                mSubTitle = getString(R.string.category_sport);
                getSupportActionBar().setSubtitle(mSubTitle);
                break;
            case R.id.category_technology:
                mSubTitle = getString(R.string.category_technology);
                getSupportActionBar().setSubtitle(mSubTitle);
                fragment = new TechNews();
                break;
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                break;
            default:
                fragment = new TopHeadlines();
                mSubTitle = getString(R.string.top_headline);
                getSupportActionBar().setSubtitle(mSubTitle);
        }

        if (fragment != null) {
            fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        } else {
            startActivity(intent);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
