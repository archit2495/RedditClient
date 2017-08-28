package com.example.architg.redditclientarchit.activity;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.architg.redditclientarchit.Adapters.RedditPostListAdapter;
import com.example.architg.redditclientarchit.Database.AppDatabase;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.RedditDisplayPost;
import com.example.architg.redditclientarchit.Model.SubredditInfo;
import com.example.architg.redditclientarchit.Network.ApiClient;
import com.example.architg.redditclientarchit.Network.ApiInterface;
import com.example.architg.redditclientarchit.Network.Loader;
import com.example.architg.redditclientarchit.R;
import com.example.architg.redditclientarchit.Utility.Utils;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by archit.g on 16/08/17.
 */

public class MainActivity extends AppCompatActivity {
    String after = "";
    RecyclerView mRecyclerView;
    ApiInterface mApiInterface;
    String url;
    LinearLayoutManager mLayoutManager;
    int mCurrentIndex = 0;
    RedditPostListAdapter mRedditPostListAdapter;
    Boolean mIsLoading = false;
    ProgressDialog mProgress;
    AppDatabase db;
    Loader mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        mRecyclerView = (RecyclerView) findViewById(R.id.feed_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").build();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRedditPostListAdapter = new RedditPostListAdapter(getApplicationContext());
        mRecyclerView.setAdapter(mRedditPostListAdapter);
        mLoader = new Loader(mRedditPostListAdapter, getApplicationContext());
        RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isPageBeingLoaded()) {
                    mProgress.show();
                    mIsLoading = true;
                    loadData();
                }
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);
        mLoader.loadData();
    }
    Boolean isPageBeingLoaded() {
        int totalItemCount = mRedditPostListAdapter.getListSize();
        int pastVisibleItems = mLayoutManager.findLastVisibleItemPosition();
        if (pastVisibleItems == totalItemCount - 1 && !mIsLoading) {
            return true;
        }
        return false;
    }

    void loadData() {
        Futures.addCallback((ListenableFuture<Boolean>) mLoader.loadData(),
                new FutureCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        mProgress.dismiss();
                        mIsLoading = false;
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }
}
