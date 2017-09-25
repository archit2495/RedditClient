package com.example.architg.redditclientarchit.network;

import com.example.architg.redditclientarchit.model.Info;
import com.example.architg.redditclientarchit.model.Root;
import com.example.architg.redditclientarchit.model.SubredditInfo;
import com.example.architg.redditclientarchit.model.SubredditListInfo;
import com.example.architg.redditclientarchit.model.SubredditRoot;
import com.example.architg.redditclientarchit.model.TrophyInfo;
import com.example.architg.redditclientarchit.model.UserInfo;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by archit.g on 16/08/17.
 */

public interface ApiInterface {
    @GET("{subreddit}/{type}.json?limit=15")
    Call<Info> getInfo(@Path("subreddit")String subreddit,@Path("type")String type,@Query("after") String afterID);
    @GET("/r/{type}/about.json")
    Call<SubredditInfo> getSubredditInfo(@Path("type") String type);
    @GET("/subreddits/popular.json")
    Call<SubredditListInfo> getSubredditListInfo();
    @GET("r/{subreddit}/comments/{article}.json")
    Call<Root> getCommentInfo(@Path("subreddit")String subreddit, @Path("article")String article, @Query("sort")String sortingCriteria);
    @GET("{url}.json")
    Call<Root> getHiddenCommentsInfo(@Path("url")String url);
    @GET("subreddits/search.json")
    Call<SubredditRoot> getSubredditSearchInfo(@Query("q") String subreddit);
    @GET("user/{username}/about.json")
    Call<UserInfo> getUserInfo(@Path("username")String username);
    @GET("user/{username}/trophies.json")
    Call<TrophyInfo> getTrophyInfo(@Path("username")String username);
    @GET("search.json")
    Call<Info> getFeedSearchInfo(@Query("q")String query,@Query("sort")String sortingCriteria,@Query("after")String after);
}
