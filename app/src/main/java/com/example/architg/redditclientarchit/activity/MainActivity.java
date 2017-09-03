package com.example.architg.redditclientarchit.activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.architg.redditclientarchit.Adapters.SubredditDisplayAdapter;
import com.example.architg.redditclientarchit.Adapters.ViewPagerAdapter;
import com.example.architg.redditclientarchit.Fragments.FeedFragment;
import com.example.architg.redditclientarchit.Fragments.SubredditFilterFragment;
import com.example.architg.redditclientarchit.Model.SubredditInfo;
import com.example.architg.redditclientarchit.Model.SubredditListInfo;
import com.example.architg.redditclientarchit.Network.ApiClient;
import com.example.architg.redditclientarchit.Network.ApiInterface;
import com.example.architg.redditclientarchit.R;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.util.List;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        //mViewPager.setOffscreenPageLimit();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.filter_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.filter:
                showSubredditFilterFragment();
                break;
            default:
                break;
        }
        return true;
    }
    public void showSubredditFilterFragment(){
        final SubredditFilterFragment subredditFilterFragment = new SubredditFilterFragment();
        Futures.addCallback(((ListenableFuture <Boolean> )subredditFilterFragment.fetch()), new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                subredditFilterFragment.show(getFragmentManager(),"");
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        /*Futures.addCallback((ListenableFuture< SubredditListInfo>)loadData(), new FutureCallback<SubredditListInfo>() {
            @Override
            public void onSuccess(SubredditListInfo result) {
                List<String> subreddits = result.getResponses();
                SubredditDisplayAdapter subredditDisplayAdapter = new SubredditDisplayAdapter(subreddits);
                subredditFilterFragment.show(getFragmentManager(),"");
                Dialog dialog = subredditFilterFragment.getDialog();
                RecyclerView recyclerView = dialog.findViewById(R.id.subreddit_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(subredditDisplayAdapter);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });*/
    }
    Future<SubredditListInfo> loadData(){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final SettableFuture<SubredditListInfo> future = SettableFuture.create();
        Call<SubredditListInfo> call = apiInterface.getSubredditListInfo();
        call.enqueue(new Callback<SubredditListInfo>() {
            @Override
            public void onResponse(Call<SubredditListInfo> call, Response<SubredditListInfo> response) {
                future.set(response.body());
            }

            @Override
            public void onFailure(Call<SubredditListInfo> call, Throwable t) {
                future.setException(t);
            }
        });
        return future;
    }
}
