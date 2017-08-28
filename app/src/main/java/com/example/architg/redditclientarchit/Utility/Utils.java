package com.example.architg.redditclientarchit.Utility;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.Post;
import com.example.architg.redditclientarchit.Model.RedditDisplayPost;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class Utils {
    public static RedditDisplayPost feedResponseToRedditDisplayPost(Context context, Info.Data.FeedResponse feedResponse) throws ParseException{
        Post post = feedResponse.getPost();
        String id = post.getId();
        String name = post.getSubreddit_name_prefixed();
        String heading = post.getTitle();
        String x =  DateUtils.formatDateTime(context, post.getCreated(), DateUtils.FORMAT_24HOUR );
        //String date = DateUtils.formatDateTime(context,post.getCreated()*1000,)
       String dateString =getDate(post.getCreated()*1000L , "dd/MM/yyyy hh:mm:ss.SSS");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        Date date =formatter.parse(dateString);
        Log.d("dsd", "timezone offset: "+date.getTimezoneOffset());
        String time = DateUtils.getRelativeTimeSpanString(post.getCreated()*1000l).toString();
//        Log.i("check", post.getCreated() *1000 + "- current: "+mytime+" "+(post.getCreated()*1000 - mytime));
        Log.i("datetime ", post.getTitle() +" :" +getDate(post.getCreated()*1000L + 330*60*1000L, "dd/MM/yyyy hh:mm:ss.SSS")+" timeStamp:"+post.getCreated());
        String contentImage = null;
        if(post.getPreview() != null){
            contentImage = post.getPreview().getImageUrl();
        }
        String sourceImage = feedResponse.getImageURL();
        String selfHelpText = post.getSelfTextHTML();
        return new RedditDisplayPost(id,name,time,heading,contentImage,sourceImage,selfHelpText);
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

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(milliSeconds);
        TimeZone timeZone = calendar.getTimeZone();
        Log.d("DateTimeZone", "getDate: "+timeZone.getDisplayName());
        return formatter.format(milliSeconds);
    }
}
