package com.example.architg.redditclientarchit.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.architg.redditclientarchit.R;
import com.example.architg.redditclientarchit.fragments.CommentFragment;
import com.example.architg.redditclientarchit.loaders.Loader;

/**
 * Created by archit.g on 07/09/17.
 */

public class CommentsActivity extends AppCompatActivity {
    String mSubreddit, mArticle, mImage, mPermalink, mTitle;
    Loader mLoader;

    enum SortingCriteria {
        TOP("Top", "top"), NEW("New", "new"), OLD("Old", "old"), RANDOM("Random", "random"), CONTROVERSIAL("Controversial", "controversial");
        private String title, type;

        private SortingCriteria(String title, String type) {
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
        mTitle = getIntent().getStringExtra("title");
        mPermalink = getIntent().getStringExtra("permalink");

        TextView textView = findViewById(R.id.post_title);
        textView.setVisibility(View.VISIBLE);
        textView.setText(mTitle);
        Toolbar toolbar = findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        //loadData();
        CommentsActivity.ViewPagerAdapter adapter = new CommentsActivity.ViewPagerAdapter(getSupportFragmentManager(), CommentsActivity.SortingCriteria.values());
        ViewPager mViewPager = findViewById(R.id.htab_viewpager);
        TabLayout mTabLayout = findViewById(R.id.htab_tabs);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    public Loader getLoader() {
        if (mLoader == null) {
            mLoader = new Loader();
            mLoader.setmSubreddit(mSubreddit);
            mLoader.setmArticle(mArticle);
            mLoader.setmPermalink(mPermalink);
        }
        return mLoader;
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        CommentsActivity.SortingCriteria[] mSortingCriteria;

        public ViewPagerAdapter(FragmentManager fm, CommentsActivity.SortingCriteria[] sortingCriteria) {
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
        public CharSequence getPageTitle(int position) {
            return mSortingCriteria[position].title;
        }

        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }

    }
}
