package com.example.architg.redditclientarchit.model;

import com.example.architg.redditclientarchit.adapters.CommentsJsonAdapter;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by archit.g on 07/09/17.
 */
@JsonAdapter(CommentsJsonAdapter.class)
public class Root {
    static public class CommentInfo {
        static public class InfoData {
            static public class Comment {
                static public class Data {
                    public String body_html,author, ups,id;
                    public Long created_utc;
                    public List<String> children;
                    @Getter
                    @Setter
                    CommentInfo replies;
                }

                @Getter
                @Setter
                private String kind;
                @Getter
                @Setter
                private Data data;
            }

            //   @SerializedName("children")
            @Getter
            @Setter
            private List<Comment> comments;

        }

        //  @SerializedName("data")
        @Getter
        @Setter
        private InfoData infoData;
    }
    public List<CommentInfo> commentInfos;
}
