package com.example.architg.redditclientarchit.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.architg.redditclientarchit.Adapters.FeedListAdapter;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.SubredditInfo;
import com.example.architg.redditclientarchit.Network.ApiClient;
import com.example.architg.redditclientarchit.Network.ApiInterface;
import com.example.architg.redditclientarchit.R;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import kotlin.internal.ProgressionUtilKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by archit.g on 16/08/17.
 */

public class MainActivity extends AppCompatActivity {
    String after = "";
    RecyclerView mRecyclerView;
    List<Info.Data.FeedResponse> mFeedResponses;
    ApiInterface mApiInterface;
    String url;
    LinearLayoutManager mLayoutManager;
    int mCurrentIndex = 0;
    FeedListAdapter mFeedListAdapter;
    Boolean mIsLoading = false;
    ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        mRecyclerView = (RecyclerView)findViewById(R.id.feed_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mFeedResponses = new ArrayList<Info.Data.FeedResponse>() ;
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
// To dismiss the dialog

        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView,dx,dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mFeedResponses.size();
                int pastVisibleItems = mLayoutManager.findLastVisibleItemPosition();
                if (pastVisibleItems == totalItemCount - 1 && !mIsLoading) {
                    mProgress.show();
                    mIsLoading = true;
                    loadData();
//                    loadSubredditDataIntoFeed();
                }
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);
        loadData();
    }
    private Future<String> loadSubredditData(String path){
        Call<SubredditInfo> call = mApiInterface.getSubredditInfo(path);
        final SettableFuture<String> future = SettableFuture.create();
        call.enqueue(new Callback<SubredditInfo>() {
            @Override
            public void onResponse(Call<SubredditInfo> call, Response<SubredditInfo> response) {
                url = response.body().getData().getIcon_img();
                future.set(url);
            }

            @Override
            public void onFailure(Call<SubredditInfo> call, Throwable t) {
                future.setException(t);
            }
        });

        return future;
//        return url;
    }
    private void loadData(){
        Call<Info> call = mApiInterface.getInfo(after);
        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(Call<Info> call, Response<Info> response) {
                if(mFeedResponses.size() == 0) {
                    mFeedResponses = response.body().getFeedResponse();
                    after = response.body().getData().getAfter();
                    mFeedListAdapter = new FeedListAdapter(mFeedResponses, getApplicationContext());
                    mRecyclerView.setAdapter(mFeedListAdapter);
                }else{
                    List<Info.Data.FeedResponse> feedResponses = response.body().getFeedResponse();
                    for(Info.Data.FeedResponse feedResponse:feedResponses){
                        mFeedResponses.add(feedResponse);
                    }
                    after = response.body().getData().getAfter();
                    mFeedListAdapter.notifyDataSetChanged();
                    mProgress.dismiss();
                }
                loadSubredditDataIntoFeed();
                mIsLoading = false;
            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_LONG).show();
                mIsLoading = false;
            }
        });
    }
    private void loadSubredditDataIntoFeed(){
        for(;mCurrentIndex < mFeedResponses.size();mCurrentIndex++){
            final Info.Data.FeedResponse feedResponse = mFeedResponses.get(mCurrentIndex);
            Futures.addCallback((ListenableFuture<String>) loadSubredditData(feedResponse.getPost().getSubreddit_name_prefixed().substring(2)), new FutureCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    if(result.length() > 0) {
                        feedResponse.setImageURL(result);
                    }else{
                        feedResponse.setImageURL(null);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    feedResponse.setImageURL(null);
                }
            });
        }
        mFeedListAdapter.notifyDataSetChanged();
    }
}
