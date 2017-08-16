package com.example.architg.redditclientarchit.Model;

import java.util.List;

import lombok.Getter;

/**
 * Created by archit.g on 16/08/17.
 */

public class Post {
    public class Preview{
        private class Type{
            private class Image{
                @Getter
                private String url;
            }
            @Getter
            private Image source;
        }

        private List<Type> images;
        public String getImageUrl(){
            return images.get(0).getSource().getUrl();
        }
    }
    @Getter
    private Preview preview;
    @Getter
    private String title;
    @Getter
    private int score;
    @Getter
    private int num_comments;
    @Getter
    private long created;
    @Getter
    private String subreddit_name_prefixed;
    @Getter
    private String domain;
}
