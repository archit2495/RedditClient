package com.example.architg.redditclientarchit.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.architg.redditclientarchit.Adapters.ViewPagerAdapter;
import com.example.architg.redditclientarchit.Fragments.SubredditFilterFragment;
import com.example.architg.redditclientarchit.R;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archit.g on 16/08/17.
 */

public class MainActivity extends AppCompatActivity{
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private List<String> mFragmentType = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();
    SharedPreferences mSharedPreferences;
    public static Bus bus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_main);
        initFragmentTypeList();
        initFragmentTitleList();
        mViewPager = findViewById(R.id.viewpager);
        setupViewPager();
        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        bus = new Bus(ThreadEnforcer.ANY);
        initSharedPref();
        //mViewPager.setOffscreenPageLimit();
    }
    private void initSharedPref(){
        mSharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("subreddit","");
        editor.commit();
    }
    private void initFragmentTypeList(){
        mFragmentType.add("hot");
        mFragmentType.add("new");
        mFragmentType.add("rising");
        mFragmentType.add("top");
        mFragmentType.add("controversial");
    }
    private void initFragmentTitleList(){
        mFragmentTitleList.add("Hot");
        mFragmentTitleList.add("New");
        mFragmentTitleList.add("Rising");
        mFragmentTitleList.add("Top");
        mFragmentTitleList.add("Controversial");
    }
    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),mFragmentType,mFragmentTitleList);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.filter_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                showSubredditFilterFragment();
                break;
            default:
                break;
        }
        return true;
    }
    public void showSubredditFilterFragment() {
        SubredditFilterFragment subredditFilterFragment = new SubredditFilterFragment();
        subredditFilterFragment.show(getFragmentManager(), "");
    }
   }
