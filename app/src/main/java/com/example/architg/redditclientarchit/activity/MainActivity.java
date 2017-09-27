package com.example.architg.redditclientarchit.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.architg.redditclientarchit.R;
import com.example.architg.redditclientarchit.adapters.SubredditDisplayAdapter;
import com.example.architg.redditclientarchit.fragments.FeedFragment;
import com.example.architg.redditclientarchit.loaders.Loader;
import com.example.architg.redditclientarchit.model.SubredditListInfo;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
    private String mSubreddit = "r/FrontPage";
    private int mSelectedPosition = 1;
    private List<SubredditChangeListener> listeners = new ArrayList<>();
    private Spinner mSpinner;
    private List<String> subreddits;
    private SubredditDisplayAdapter subredditAdapter;
    private String enteredSubreddit = "";


    static {
        bus = new Bus(ThreadEnforcer.ANY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initSubredditSpinner();
        findViews();
        bindViews();
        mViewPager.setOffscreenPageLimit(5);
        loadData();
    }
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        if(intent != null && intent.getExtras() != null){
            mSubreddit = intent.getExtras().getString("subreddit","r/FrontPage");
            initSubredditSpinner();
            loadData();
        }
    }
    public void register(SubredditChangeListener subredditChangeListener) {
        listeners.add(subredditChangeListener);
        Logger.getLogger("kdvnfkd").info(listeners.size() + " size");
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

    private void initSubredditSpinner() {
        mSpinner = findViewById(R.id.spinner);
        subreddits = new ArrayList<>();
        subreddits.add("Enter a subreddit");
        if(!mSubreddit.equals("r/FrontPage")){
            subreddits.add(mSubreddit.substring(2));
        }
        subreddits.add("Frontpage");
        subreddits.add("All");
        subreddits.add("Popular");
        subredditAdapter = new SubredditDisplayAdapter(getApplicationContext(), R.layout.spinner_item_main,subreddits);
        //subredditAdapter.addAll();
        mSpinner.setAdapter(subredditAdapter);
        mSpinner.setSelection(1);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String subreddit = (String) adapterView.getItemAtPosition(i);

                if (i == 0) {
                    showDialog();
                } else {
                    mSelectedPosition = i;
                    updateFragments(subreddit);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

    public Loader getLoader() {
        if (mLoader == null) {
            mLoader = new Loader();
            mLoader.setmSubreddit(mSubreddit);
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
                final List<String> fetchedSubreddits = result.getResponses();
                for (int i = 0; i < fetchedSubreddits.size(); i++) {
                    fetchedSubreddits.set(i,fetchedSubreddits.get(i).substring(2));
                }
                subredditAdapter.update(fetchedSubreddits);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void updateFragments(String subreddit) {
        mLoader.setmSubreddit("r/" + subreddit);
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).updateView();
        }
    }

    public void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Go to subreddit");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 40;
        input.setLayoutParams(params);
        container.addView(input);
        builder.setView(container);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enteredSubreddit = input.getText().toString();
                if(enteredSubreddit != null && enteredSubreddit.length() > 0) {
                    subredditAdapter.updateLastItem(enteredSubreddit);
                    mSpinner.setSelection(mSpinner.getCount() - 1);
                    mSelectedPosition = mSpinner.getCount() - 1;
                }else{
                    mSpinner.setSelection(mSelectedPosition);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mSpinner.setSelection(mSelectedPosition);
            }
        });

        builder.show();

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

    public interface SubredditChangeListener {
        public void updateView();
    }
}
