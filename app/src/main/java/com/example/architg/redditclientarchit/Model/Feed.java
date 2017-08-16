package com.example.architg.redditclientarchit.Model;

/**
 * Created by archit.g on 11/08/17.
 */

public class Feed {
    private String title, heading, time, upvotes, comments, sourceImageUrl, contentImageUrl;

    public String getTitle() {
        return title;
    }

    public String getHeading() {
        return heading;
    }

    public Feed(String title, String heading, String time, String upvotes, String comments, String sourceImageUrl, String contentImageUrl) {
        this.title = title;
        this.heading = heading;
        this.time = time;
        this.upvotes = upvotes;
        this.comments = comments;
        this.sourceImageUrl = sourceImageUrl;
        this.contentImageUrl = contentImageUrl;
    }

    public String getTime() {
        return time;

    }

    public String getUpvotes() {
        return upvotes;
    }

    public String getComments() {
        return comments;
    }

    public String getSourceImageUrl() {
        return sourceImageUrl;
    }

    public String getContentImageUrl() {
        return contentImageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUpvotes(String upvotes) {
        this.upvotes = upvotes;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setSourceImageUrl(String sourceImageUrl) {
        this.sourceImageUrl = sourceImageUrl;
    }

    public void setContentImageUrl(String contentImageUrl) {
        this.contentImageUrl = contentImageUrl;
    }
}

