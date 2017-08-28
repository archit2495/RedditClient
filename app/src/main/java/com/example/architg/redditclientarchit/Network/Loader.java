package com.example.architg.redditclientarchit.Network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.architg.redditclientarchit.Adapters.RedditPostListAdapter;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.RedditDisplayPost;
import com.example.architg.redditclientarchit.Model.SubredditInfo;
import com.example.architg.redditclientarchit.Utility.Utils;
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
 * Created by archit.g on 25/08/17.
 */

public class Loader {
    String after = "";
    RedditPostListAdapter mRedditPostListAdapter;
    ApiInterface mApiInterface;
    Context mContext;
    public Loader(RedditPostListAdapter redditPostListAdapter,Context context){
        mRedditPostListAdapter = redditPostListAdapter;
        mContext = context;
    }
    public Future<Boolean> loadData(){
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Info> call = mApiInterface.getInfo(after);
        final SettableFuture<Boolean> future = SettableFuture.create();
        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(Call<Info> call, Response<Info> response) {
                List<RedditDisplayPost> redditDisplayPosts = Utils.convertFeedResposeListToRedditDisplayPostList(mContext, response.body().getFeedResponse());
                int size = redditDisplayPosts.size();
                after = response.body().getData().getAfter();
                mRedditPostListAdapter.update(redditDisplayPosts);
                future.set(true);
              //  mProgress.dismiss();
                //  Log.i("here","befor sub");
                loadSubredditDataIntoFeed(size);
               // mIsLoading = false;
            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                Toast.makeText(mContext,"failed",Toast.LENGTH_LONG).show();
                //mIsLoading = false;
                future.set(true);
            }
        });
        return future;
    }
    private void loadSubredditDataIntoFeed(int size){
        List<RedditDisplayPost> redditDisplayPosts = mRedditPostListAdapter.getmRedditDisplayPostsList(size);
        for(int i = 0;i < redditDisplayPosts.size();i++){
            final RedditDisplayPost redditDisplayPost = redditDisplayPosts.get(i);
            final int position = i;
            Futures.addCallback((ListenableFuture<String>) loadSubredditData(redditDisplayPost.getName().substring(2)), new FutureCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    mRedditPostListAdapter.updateSourceImage(result,position);
                }

                @Override
                public void onFailure(Throwable t) {
                    mRedditPostListAdapter.updateSourceImage(null,position);
                }
            });
        }

        // db.postDao().insertAll(mRedditPostListAdapter.getmRedditDisplayPostsList());
    }
    private Future<String> loadSubredditData(String path){
        Call<SubredditInfo> call = mApiInterface.getSubredditInfo(path);
        final SettableFuture<String> future = SettableFuture.create();
        call.enqueue(new Callback<SubredditInfo>() {
            @Override
            public void onResponse(Call<SubredditInfo> call, Response<SubredditInfo> response) {
                String url = response.body().getData().getIcon_img();
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
}
