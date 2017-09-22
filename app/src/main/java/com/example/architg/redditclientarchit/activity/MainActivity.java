package com.example.architg.redditclientarchit.activity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.architg.redditclientarchit.R;
import com.example.architg.redditclientarchit.adapters.SubredditDisplayAdapter;
import com.example.architg.redditclientarchit.fragments.FeedFragment;
import com.example.architg.redditclientarchit.fragments.SubredditFilterFragment;
import com.example.architg.redditclientarchit.loaders.Loader;
import com.example.architg.redditclientarchit.model.SubredditListInfo;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.List;

import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;

/**
 * Created by archit.g on 16/08/17.
 */

public class MainActivity extends AppCompatActivity {
    enum FragmentCategory {
        HOT("Hot", "hot"), NEW("New", "new"), RISING("Rising", "rising"), TOP("Top", "top"), CONTROVERSIAL("Controversial", "controversial");
        private String title, type;

        private FragmentCategory(String title, String type) {
            this.title = title;
            this.type = type;
        }
    }

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    public static Bus bus;
    public Loader mLoader;
    private int mSelectedPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        bus = new Bus(ThreadEnforcer.ANY);
        mViewPager.setOffscreenPageLimit(5);
    }

    private void initViews() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentCategory.values());
    }

    private void findViews() {
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tabs);
    }

    private void bindViews() {
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        ImageView searchImageView = findViewById(R.id.search);
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchActivity();
            }
        });
    }

    public Loader getLoader() {
        if (mLoader == null) {
            mLoader = new Loader();
          //  mLoader.setmSubreddit("Frontpage");
        }
        return mLoader;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.filter_menu, menu);
        return true;
    }

    private void startSearchActivity() {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
    }

    void loadData() {
        Loader loader = getLoader();
        Futures.addCallback((ListenableFuture<SubredditListInfo>) loader.loadSubredditFilters(), new FutureCallback<SubredditListInfo>() {
            @Override
            public void onSuccess(SubredditListInfo result) {
                final List<String> subreddits = result.getResponses();
                subreddits.add(0, "Enter a subreddit");
                subreddits.add(1, "  FrontPage");
                findViews();
                initViews();
                bindViews();
                final Spinner spinner = findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_main) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);
                        if (position == 1) {
                            //((TextView) v.findViewById(R.id.text1)).setText("");
                            ((TextView) v.findViewById(R.id.text1)).setHint(getItem(1)); //"Hint to be displayed"
                            ((TextView) v.findViewById(R.id.text1)).setHintTextColor(Color.parseColor("#ffffff"));
                        }
                        return v;
                    }

                    @Override
                    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
                        //  LayoutInflater inflater = getLayoutInflater();
                        View spinnerItem;
                        if (position == 0) {
                            spinnerItem = LayoutInflater.from(getApplicationContext()).inflate(R.layout.enter_subreddit_single_item_text_view, null);
                            ((TextView) spinnerItem.findViewById(R.id.text1)).setText(subreddits.get(position));
                            if (mSelectedPosition == position) {

                            }
                        } else {
                            spinnerItem = LayoutInflater.from(getApplicationContext()).inflate(R.layout.simple_text_view, null);
                            ((TextView) spinnerItem).setText(subreddits.get(position).substring(2));
                        }
                        return spinnerItem;
                    }

                    @Override
                    public int getCount() {
                        return super.getCount(); // you dont display last item. It is used as hint.
                    }

                };
                //adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                adapter.addAll(subreddits);
                spinner.setAdapter(adapter);
                spinner.setSelection(1);
                spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String subreddit = (String) adapterView.getItemAtPosition(i);
                        if (i == 0) {
                            showDialog();
                        } else if (!subreddit.equalsIgnoreCase("FrontPage")) {
                            bus.post(subreddit);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void showDialog() {

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        FragmentCategory[] mFragmentCategory;

        public ViewPagerAdapter(FragmentManager fm, FragmentCategory[] fragmentCategory) {
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
        public CharSequence getPageTitle(int position) {
            return mFragmentCategory[position].title;
        }

        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }

    }

}
