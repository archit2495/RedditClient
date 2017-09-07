package com.example.architg.redditclientarchit.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

/**
 * Created by archit.g on 07/09/17.
 */

public class CommentInfo {
    public class InfoData {
        public class Comment {
            public class Data {
                String body_html;
                @Getter
                List<CommentInfo> replies;
            }

            @Getter
            private Data data;
        }

        @SerializedName("children")
        @Getter
        private List<Comment> comments;
    }
    @SerializedName("data")
    @Getter
    private InfoData infoData;
}
