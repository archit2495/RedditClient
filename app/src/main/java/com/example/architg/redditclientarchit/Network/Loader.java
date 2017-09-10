package com.example.architg.redditclientarchit.Network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.Root;
import com.example.architg.redditclientarchit.Model.SubredditInfo;
import com.example.architg.redditclientarchit.Model.SubredditListInfo;
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
    ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
    private String mSubreddit = "happy";
    private String mArticle = "6yze1g";

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
    public Future<Root> loadComments(String sortingCriteria){
        Call<Root> call = mApiInterface.getCommentInfo(mSubreddit,mArticle,sortingCriteria);
        final SettableFuture<Root> future = SettableFuture.create();
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                future.set(response.body());
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
            }
        });
        return future;
    }
}
