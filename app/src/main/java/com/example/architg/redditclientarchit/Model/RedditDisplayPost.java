package com.example.architg.redditclientarchit.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by archit.g on 23/08/17.
 */
 @Entity
public class RedditDisplayPost {
    @PrimaryKey
    private String id;
    private String name;
    private String heading;
    private String time;
    @ColumnInfo(name = "content_image")
    private String contentImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContentImage() {
        return contentImage;
    }

    public void setContentImage(String contentImage) {
        this.contentImage = contentImage;
    }

    public String getSourceImage() {
        return sourceImage;
    }

    public void setSourceImage(String sourceImage) {
        this.sourceImage = sourceImage;
    }

    @ColumnInfo(name = "source_image")
    private String sourceImage;

    public String getSelfHelpText() {
        return selfHelpText;
    }

    public void setSelfHelpText(String selfHelpText) {
        this.selfHelpText = selfHelpText;
    }

    @ColumnInfo(name = "self_help_text")
    private String selfHelpText;
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
