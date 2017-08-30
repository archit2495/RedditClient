package com.example.architg.redditclientarchit.Utility;
import android.content.Context;
import android.text.format.DateUtils;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.Post;
import com.example.architg.redditclientarchit.Model.RedditDisplayPost;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;



public class Utils {
    public static RedditDisplayPost feedResponseToRedditDisplayPost(Context context, Info.Data.FeedResponse feedResponse) throws ParseException{
        Post post = feedResponse.getPost();
        String id = post.getId();
        String name = post.getSubreddit_name_prefixed();
        String heading = post.getTitle();
        String time = DateUtils.getRelativeTimeSpanString(post.getCreated()*1000l).toString();
        String contentImageDetail = null;
        if(post.getPreview() != null){
            contentImageDetail = post.getPreview().getImageUrl();
        }
        String contentImageThumbnail = post.getThumbnail();
        String sourceImage = feedResponse.getImageURL();
        String selfHelpText = post.getSelfTextHTML();
        return new RedditDisplayPost(id,name,time,heading,contentImageDetail,contentImageDetail,sourceImage,selfHelpText);
    }
    public static List<RedditDisplayPost> convertFeedResposeListToRedditDisplayPostList(Context context, List<Info.Data.FeedResponse> feedResponses){
        List<RedditDisplayPost> redditDisplayPosts = new ArrayList<>();
        for(Info.Data.FeedResponse feedResponse:feedResponses){
            try {
                redditDisplayPosts.add(feedResponseToRedditDisplayPost(context, feedResponse));
            }catch (Exception e){}
        }
        return redditDisplayPosts;
    }
}
