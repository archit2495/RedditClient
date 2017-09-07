package com.example.architg.redditclientarchit.Network;

import com.example.architg.redditclientarchit.Model.CommentInfo;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.SubredditInfo;
import com.example.architg.redditclientarchit.Model.SubredditListInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by archit.g on 16/08/17.
 */

public interface ApiInterface {
    @GET("{subreddit}/{type}.json?limit=10")
    Call<Info> getInfo(@Path("subreddit")String subreddit,@Path("type")String type,@Query("after") String afterID);
    @GET("/r/{type}/about.json")
    Call<SubredditInfo> getSubredditInfo(@Path("type") String type);
    @GET("/subreddits/popular.json")
    Call<SubredditListInfo> getSubredditListInfo();
    @GET("r/{subreddit}/comments/{article}.json")
    Call<CommentInfo> getCommentInfo(@Path("subreddit")String subreddit,@Path("article")String article,@Query("sort")String sortingCriteria);
}
