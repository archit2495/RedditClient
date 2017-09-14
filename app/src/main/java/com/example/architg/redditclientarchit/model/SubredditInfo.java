package com.example.architg.redditclientarchit.model;

import lombok.Getter;

/**
 * Created by archit.g on 17/08/17.
 */

public class SubredditInfo {
    public class Data{
        @Getter
        private String icon_img;
    }
    @Getter
    private Data data;
}
