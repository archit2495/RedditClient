package com.example.architg.redditclientarchit.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.architg.redditclientarchit.Adapters.RedditPostListAdapter;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.RedditDisplayPost;
import com.example.architg.redditclientarchit.Model.SubredditInfo;
import com.example.architg.redditclientarchit.Network.ApiClient;
import com.example.architg.redditclientarchit.Network.ApiInterface;
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
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        mRecyclerView = (RecyclerView)findViewById(R.id.feed_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
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
                int totalItemCount = mRedditPostListAdapter.getListSize();
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
        mRedditPostListAdapter = new RedditPostListAdapter(getApplicationContext());
        mRecyclerView.setAdapter(mRedditPostListAdapter);
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
                List<RedditDisplayPost> redditDisplayPosts = Utils.convertFeedResposeListToRedditDisplayPostList(response.body().getFeedResponse());
                after = response.body().getData().getAfter();
                mRedditPostListAdapter.update(redditDisplayPosts);
                mProgress.dismiss();
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
        List<RedditDisplayPost> redditDisplayPosts = mRedditPostListAdapter.getmRedditDisplayPostsList();
        int index = mCurrentIndex;
        for(;mCurrentIndex < redditDisplayPosts.size();mCurrentIndex++){
            final RedditDisplayPost redditDisplayPost = redditDisplayPosts.get(mCurrentIndex);
            Futures.addCallback((ListenableFuture<String>) loadSubredditData(redditDisplayPost.getName().substring(2)), new FutureCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    if(result != null && result.length() > 0) {
                        redditDisplayPost.setSourceImage(result);
                    }else{
                        redditDisplayPost.setSourceImage(null);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    redditDisplayPost.setSourceImage(null);
                }
            });
        }
        mRedditPostListAdapter.updateSourceImage(index,redditDisplayPosts.size() - index);
    }
}
