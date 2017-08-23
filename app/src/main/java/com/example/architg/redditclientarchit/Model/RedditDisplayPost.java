package com.example.architg.redditclientarchit.Model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by archit.g on 23/08/17.
 */
@Getter @Setter
public class RedditDisplayPost {
    private String id,name,heading,time,contentImage,sourceImage,selfHelpText;
    public RedditDisplayPost(String id,String name,String time,String heading,String contentImage,String sourceImage,String selfHelpText){
        this.selfHelpText = selfHelpText;
        this.contentImage = contentImage;
        this.name = name;
        this.sourceImage = sourceImage;
        this.heading = heading;
        this.time = time;
        this.id = id;
    }

}
