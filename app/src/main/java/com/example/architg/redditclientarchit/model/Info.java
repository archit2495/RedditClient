package com.example.architg.redditclientarchit.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by archit.g on 16/08/17.
 */

public class Info {
    public class Data {
        public class FeedResponse {
            @SerializedName("data")
            public Post post;

            public Post getPost() {
                return post;
            }

            @Getter
            @Setter
            public String imageURL;
        }

        @Getter
        @SerializedName("children")
        public List<FeedResponse> feedResponses;
        @Getter
        public  String after;

    }

    @Getter
    public Data data;

    public List<Data.FeedResponse> getFeedResponse() {
        return data.feedResponses;

    }

}
