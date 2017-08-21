package com.example.architg.redditclientarchit.Network;

import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.SubredditInfo;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by archit.g on 16/08/17.
 */

public interface ApiInterface {
    @GET("/hot.json")
    Call<Info> getInfo(@Query("after") String afterID);
    @GET("/{type}/about.json")
    Call<SubredditInfo> getSubredditInfo(@Path("type") String type);
}