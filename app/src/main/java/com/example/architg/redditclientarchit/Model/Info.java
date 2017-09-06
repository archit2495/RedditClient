package com.example.architg.redditclientarchit.Model;

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
            private Post post;

            public Post getPost() {
                return post;
            }

            @Getter
            @Setter
            private String imageURL;
        }

        @Getter
        @SerializedName("children")
        private List<FeedResponse> feedResponses;
        @Getter
        private String after;

    }

    @Getter
    private Data data;

    public List<Data.FeedResponse> getFeedResponse() {
        return data.feedResponses;

    }

}
