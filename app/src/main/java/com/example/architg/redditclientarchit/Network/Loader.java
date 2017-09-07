package com.example.architg.redditclientarchit.Network;

import android.support.annotation.NonNull;

import com.example.architg.redditclientarchit.Model.CommentInfo;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.SubredditInfo;
import com.example.architg.redditclientarchit.Model.SubredditListInfo;
import com.google.common.util.concurrent.SettableFuture;

import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by archit.g on 25/08/17.
 */

public class Loader {
    ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
    private String mSubreddit = "";
    private String mArticle = "";

    public String getmArticle() {
        return mArticle;
    }

    public void setmArticle(String mArticle) {
        this.mArticle = mArticle;
    }

    public String getmSubreddit() {
        return mSubreddit;
    }

    public void setmSubreddit(String mSubreddit) {
        this.mSubreddit = mSubreddit;
    }

    public Future<Info> loadData(String type, String after) {
        Call<Info> call = mApiInterface.getInfo(mSubreddit, type, after);
        final SettableFuture<Info> future = SettableFuture.create();
        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(@NonNull Call<Info> call, @NonNull Response<Info> response) {
                if (response.body() != null && response.body().getFeedResponse() != null && !response.body().getFeedResponse().isEmpty()) {
                    future.set(response.body());
                } else {
                    future.setException(new RuntimeException());
                }
            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                future.setException(t);
            }
        });
        return future;
    }

    public Future<String> loadSubredditData(String path) {
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
    }
    public Future<SubredditListInfo> loadSubredditFilters(){
        Call<SubredditListInfo> call = mApiInterface.getSubredditListInfo();
        final SettableFuture<SubredditListInfo> future = SettableFuture.create();
        call.enqueue(new Callback<SubredditListInfo>() {
            @Override
            public void onResponse(Call<SubredditListInfo> call, Response<SubredditListInfo> response) {
                future.set(response.body());
            }

            @Override
            public void onFailure(Call<SubredditListInfo> call, Throwable t) {future.setException(t);}
        });
        return future;
    }
    public Future<CommentInfo> loadComments(String sortingCriteria){
        Call<CommentInfo> call = mApiInterface.getCommentInfo(mSubreddit,mArticle,sortingCriteria);
        final SettableFuture<CommentInfo> future = SettableFuture.create();
        call.enqueue(new Callback<CommentInfo>() {
            @Override
            public void onResponse(Call<CommentInfo> call, Response<CommentInfo> response) {
                future.set(response.body());
            }

            @Override
            public void onFailure(Call<CommentInfo> call, Throwable t) {

            }
        });
        return future;
    }
}
