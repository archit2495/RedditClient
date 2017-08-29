package com.example.architg.redditclientarchit.activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.architg.redditclientarchit.Adapters.ViewPagerAdapter;
import com.example.architg.redditclientarchit.Fragments.FeedFragment;
import com.example.architg.redditclientarchit.R;

/**
 * Created by archit.g on 16/08/17.
 */

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_main);
        mViewPager = findViewById(R.id.viewpager);
        setupViewPager();
        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
    }

    private FeedFragment getFragment(String type) {
        FeedFragment feedFragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        feedFragment.setArguments(bundle);
        return feedFragment;
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(getFragment("hot"), "Hot");
        adapter.addFragment(getFragment("new"), "New");
        adapter.addFragment(getFragment("rising"), "Rising");
        adapter.addFragment(getFragment("top"), "Top");
        adapter.addFragment(getFragment("controversial"), "Controversial");
        mViewPager.setAdapter(adapter);
    }



}
