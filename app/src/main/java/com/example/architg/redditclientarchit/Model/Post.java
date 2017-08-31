package com.example.architg.redditclientarchit.Model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

/**
 * Created by archit.g on 16/08/17.
 */

public class Post {
    public class Preview {
        private class Type {
            private class Image {
                @Getter
                private String url;
            }

            @Getter
            private Image source;
        }

        public List<Type> images;

        public String getImageUrl() {
            return images.get(0).getSource().getUrl();
        }
    }

    @Getter
    private Preview preview;
    @Getter
    private String thumbnail;
    @Getter
    private String title;
    @Getter
    private int score;
    @Getter
    private int num_comments;
    @Getter
    @SerializedName("created_utc")
    private long created;
    @Getter
    private String subreddit_name_prefixed;
    @Getter
    private String domain;
    @Getter
    @SerializedName("is_self")
    private Boolean IsSelf;
    @Getter
    @SerializedName("selftext_html")
    private String SelfTextHTML;
    @Getter
    private String url;
    @Getter
    private String id;
}
