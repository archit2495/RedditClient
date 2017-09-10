package com.example.architg.redditclientarchit.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.architg.redditclientarchit.Fragments.CommentFragment;
import com.example.architg.redditclientarchit.Fragments.FeedFragment;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.Root;
import com.example.architg.redditclientarchit.Network.Loader;
import com.example.architg.redditclientarchit.R;
import com.example.architg.redditclientarchit.RedditApplication;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

/**
 * Created by archit.g on 07/09/17.
 */

public class CommentsActivity extends AppCompatActivity{
    String mSubreddit,mArticle,mImage;
    Loader mLoader;
    enum SortingCriteria{
        TOP("Top","top"),NEW("New","new"),OLD("Old","old"),RANDOM("Random","random"),CONTROVERSIAL("Controversial","controversial");
        private String title,type;
        private SortingCriteria(String title,String type){
            this.title = title;
            this.type = type;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_view_pager);
        mSubreddit = getIntent().getStringExtra("subreddit");
        mArticle = getIntent().getStringExtra("article");
        mImage = getIntent().getStringExtra("image");
        ImageView imageView = findViewById(R.id.post_image);
        loadImage(imageView);
        //loadData();
        CommentsActivity.ViewPagerAdapter adapter = new CommentsActivity.ViewPagerAdapter(getSupportFragmentManager(), CommentsActivity.SortingCriteria.values());
        ViewPager mViewPager = findViewById(R.id.htab_viewpager);
        TabLayout mTabLayout = findViewById(R.id.htab_tabs);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
    private void loadImage(ImageView imageView){
        Glide.with(getApplicationContext()).load(mImage).into(imageView);
    }
    public Loader getLoader(){
        if(mLoader == null){
            mLoader = new Loader();
            mLoader.setmSubreddit(mSubreddit);
            mLoader.setmArticle(mArticle);

        }
        return mLoader;
    }
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        CommentsActivity.SortingCriteria[] mSortingCriteria;
        public ViewPagerAdapter(FragmentManager fm, CommentsActivity.SortingCriteria[] sortingCriteria){
            super(fm);
            mSortingCriteria = sortingCriteria;
        }
        @Override
        public CommentFragment getItem(int position) {
            String type = mSortingCriteria[position].type;
            CommentFragment commentFragment = CommentFragment.getInstance(type);
            return commentFragment;
        }
        @Override
        public int getCount() {
            return mSortingCriteria.length;
        }
        @Override
        public CharSequence getPageTitle(int position){
            return mSortingCriteria[position].title;
        }
        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }

    }
}
