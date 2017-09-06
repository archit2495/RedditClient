package com.example.architg.redditclientarchit.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateUtils;

import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.Post;
import com.example.architg.redditclientarchit.Model.RedditDisplayPost;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;



public class Utils {
    static SharedPreferences mSharedPreferences;
    public static void initUtils(Activity activity){
        mSharedPreferences = activity.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    }
    public static RedditDisplayPost feedResponseToRedditDisplayPost(Info.Data.FeedResponse feedResponse) throws ParseException{
        Post post = feedResponse.getPost();
        String id = post.getId();
        String name = post.getSubreddit_name_prefixed();
        String heading = post.getTitle();
        String time = DateUtils.getRelativeTimeSpanString(post.getCreated()*1000l).toString();
        String contentImageDetail = null;
        if(post.getPreview() != null){
            contentImageDetail = post.getPreview().getImageUrl();
        }
        String sourceImage = feedResponse.getImageURL();
        String selfHelpText = post.getSelfTextHTML();
        String url = post.getUrl();
        return new RedditDisplayPost(id,name,time,heading,contentImageDetail,contentImageDetail,sourceImage,selfHelpText,url);
    }
    public static List<RedditDisplayPost> convertFeedResposeListToRedditDisplayPostList(List<Info.Data.FeedResponse> feedResponses){
        List<RedditDisplayPost> redditDisplayPosts = new ArrayList<>();
        for(Info.Data.FeedResponse feedResponse:feedResponses){
            try {
                redditDisplayPosts.add(feedResponseToRedditDisplayPost(feedResponse));
            }catch (Exception e){}
        }
        return redditDisplayPosts;
    }
}
