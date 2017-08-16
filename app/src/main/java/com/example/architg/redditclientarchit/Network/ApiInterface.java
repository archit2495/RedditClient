package com.example.architg.redditclientarchit.Network;

import com.example.architg.redditclientarchit.Model.Info;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by archit.g on 16/08/17.
 */

public interface ApiInterface {
    @GET("/hot.json")
    Call<Info> getInfo();
}
