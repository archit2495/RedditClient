package com.example.architg.redditclientarchit.Model;

/**
 * Created by archit.g on 23/08/17.
 */
public class RedditDisplayPost {
    private String id;
    private String name;
    private String heading;
    private String time;
    private String contentImageThumbnail;
    private String contentImageDetail;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;
    public String getContentImageThumbnail() {
        return contentImageThumbnail;
    }

    public void setContentImageThumbnail(String contentImageThumbnail) {
        this.contentImageThumbnail = contentImageThumbnail;
    }

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

    public String getContentImageDetail() {
        return contentImageThumbnail;
    }

    public void setContentImageDetail(String contentImageDetail) {
        this.contentImageDetail = contentImageDetail;
    }

    public String getSourceImage() {
        return sourceImage;
    }

    public void setSourceImage(String sourceImage) {
        this.sourceImage = sourceImage;
    }

    private String sourceImage;

    public String getSelfHelpText() {
        return selfHelpText;
    }

    public void setSelfHelpText(String selfHelpText) {
        this.selfHelpText = selfHelpText;
    }

    private String selfHelpText;

    public RedditDisplayPost(String id,String name,String time,String heading,String contentImageDetail,String contentImageThumbnail,String sourceImage,String selfHelpText,String url){
        this.url = url;
        this.selfHelpText = selfHelpText;
        this.contentImageDetail = contentImageDetail;
        this.name = name;
        this.sourceImage = sourceImage;
        this.heading = heading;
        this.time = time;
        this.id = id;
        this.contentImageThumbnail = contentImageThumbnail;
    }

}
