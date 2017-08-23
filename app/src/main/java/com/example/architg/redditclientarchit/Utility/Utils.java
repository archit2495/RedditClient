package com.example.architg.redditclientarchit.Utility;

import android.text.format.DateUtils;

import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.Post;
import com.example.architg.redditclientarchit.Model.RedditDisplayPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archit.g on 23/08/17.
 */

public class Utils {
    public static RedditDisplayPost feedResponseToRedditDisplayPost(Info.Data.FeedResponse feedResponse){
        Post post = feedResponse.getPost();
        String id = post.getId();
        String name = post.getSubreddit_name_prefixed();
        String heading = post.getTitle();
        String time = (String) DateUtils.getRelativeTimeSpanString(post.getCreated());
        String contentImage = null;
        if(post.getPreview() != null){
            contentImage = post.getPreview().getImageUrl();
        }
        String sourceImage = feedResponse.getImageURL();
        String selfHelpText = post.getSelfTextHTML();
        return new RedditDisplayPost(id,name,time,heading,contentImage,sourceImage,selfHelpText);
    }
    public static List<RedditDisplayPost> convertFeedResposeListToRedditDisplayPostList(List<Info.Data.FeedResponse> feedResponses){
        List<RedditDisplayPost> redditDisplayPosts = new ArrayList<>();
        for(Info.Data.FeedResponse feedResponse:feedResponses){
            redditDisplayPosts.add(feedResponseToRedditDisplayPost(feedResponse));
        }
        return redditDisplayPosts;
    }
}
