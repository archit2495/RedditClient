package com.example.architg.redditclientarchit.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.architg.redditclientarchit.Fragments.FeedFragment;
import com.example.architg.redditclientarchit.Fragments.SubredditFilterFragment;
import com.example.architg.redditclientarchit.Network.Loader;
import com.example.architg.redditclientarchit.R;
import com.example.architg.redditclientarchit.Utility.Utils;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
/**
 * Created by archit.g on 16/08/17.
 */

public class MainActivity extends AppCompatActivity{
    enum FragmentCategory{
        HOT("Hot","hot"),NEW("New","new"),RISING("Rising","rising"),TOP("Top","top"),CONTROVERSIAL("Controversial","controversial");
        private String title,type;
        private FragmentCategory(String title,String type){
            this.title = title;
            this.type = type;
        }
    }
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    public static Bus bus;
    public Loader mLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.initUtils(this);
        findViews();
        initViews();
        bindViews();
        bus = new Bus(ThreadEnforcer.ANY);
        //mViewPager.setOffscreenPageLimit();
    }
    private void initViews(){
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),FragmentCategory.values());
    }
    private void findViews(){
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tabs);
    }
    private void bindViews(){
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
    public Loader getLoader(){
        if(mLoader == null){
            mLoader = new Loader();
        }
        return mLoader;
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
        SubredditFilterFragment subredditFilterFragment = SubredditFilterFragment.getInstance();
        subredditFilterFragment.show(getFragmentManager(), "");
    }
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        FragmentCategory[] mFragmentCategory;
        public ViewPagerAdapter(FragmentManager fm,FragmentCategory[] fragmentCategory){
            super(fm);
            mFragmentCategory = fragmentCategory;
        }
        @Override
        public FeedFragment getItem(int position) {
            String type = mFragmentCategory[position].type;
            FeedFragment feedFragment = FeedFragment.getInstance(type);
            return feedFragment;
        }
        @Override
        public int getCount() {
            return mFragmentCategory.length;
        }
        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentCategory[position].title;
        }
        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }

    }

}
