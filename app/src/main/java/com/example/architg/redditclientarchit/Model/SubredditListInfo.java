package com.example.architg.redditclientarchit.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Created by archit.g on 01/09/17.
 */

public class SubredditListInfo {
    public class Data{
        public class FeedResponse{
            public class Name{
                @Getter
                @SerializedName("display_name_prefixed")
                private String subreddit_name;
            }
            @Getter
            @SerializedName("data")
            private Name name;
        }
        @SerializedName("children")
        public List<FeedResponse> feedResponses;
    }
    @Getter
    private Data data;
    public List<String> getResponses(){
        List<String> result = new ArrayList<>();
        for(Data.FeedResponse feedResponse:data.feedResponses){
            result.add(feedResponse.getName().getSubreddit_name());
        }
        return result;
    }

}
