package com.example.architg.redditclientarchit.Adapters;

import android.util.Log;

import com.example.architg.redditclientarchit.Model.Root;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by archit.g on 09/09/17.
 */

public class CommentsJsonAdapter extends TypeAdapter<Root> {

    @Override
    public void write(JsonWriter out, Root value) throws IOException {

    }

    @Override
    public Root read(JsonReader in) throws IOException {
        List<Root.CommentInfo> commentInfos = new ArrayList<>();
        in.beginArray();
        for (int i = 0; i < 2; i++) {
            commentInfos.add(getCommentInfo(in));
        }
        in.endArray();
        Root root = new Root();
        root.commentInfos = commentInfos;
        return root;
    }

    public Root.CommentInfo getCommentInfo(JsonReader in) throws IOException {
        Root.CommentInfo commentInfo = new Root.CommentInfo();
        in.beginObject();
        while (in.hasNext()) {
            if (in.nextName().equals("data")) {
                Root.CommentInfo.InfoData infoData = getInfoData(in);
                commentInfo.setInfoData(infoData);
            } else {
                in.skipValue();
            }
        }
        in.endObject();
        return commentInfo;
    }

    public Root.CommentInfo.InfoData getInfoData(JsonReader in) throws IOException {
        Root.CommentInfo.InfoData infoData = new Root.CommentInfo.InfoData();
        List<Root.CommentInfo.InfoData.Comment> comments = new ArrayList<>();
        in.beginObject();
        while (in.hasNext()) {
            if (in.nextName().equals("children")) {
                in.beginArray();
                while (in.hasNext()) {
                    Root.CommentInfo.InfoData.Comment comment = new Root.CommentInfo.InfoData.Comment();
                    in.beginObject();
                    while (in.hasNext()) {
                        String name = in.nextName();
                        if (name.equals("data")) {
                            comment.setData(loadData(in));
                        } else if (name.equals("kind")) {
                            comment.setKind(in.nextString());
                        } else {
                            in.skipValue();
                        }
                    }
                    in.endObject();
                    comments.add(comment);
                }
                in.endArray();
            } else {
                in.skipValue();
            }
        }
        in.endObject();
        infoData.setComments(comments);
        return infoData;
    }

    public Root.CommentInfo.InfoData.Comment.Data loadData(JsonReader in) throws IOException {

        Root.CommentInfo.InfoData.Comment.Data data = new Root.CommentInfo.InfoData.Comment.Data();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals("replies")) {
                JsonToken token = in.peek();
                if (token.equals(JsonToken.STRING)) {
                    data.setReplies(null);
                    in.nextString();
                } else {
                    data.setReplies(getCommentInfo(in));
                }
            } else if (name.equals("body")) {
                data.body_html = in.nextString();
            } else if (name.equals("author")) {
                data.author = in.nextString();
            } else if (name.equals("ups")) {
                data.ups = in.nextString();
            } else if (name.equals("created_utc")) {
                data.created_utc = in.nextLong();
            } else if (name.equals("id")) {
                data.id = in.nextString();
            } else if (name.equals("children")) {
                in.beginArray();
                List<String> children = new ArrayList<>();
                while (in.hasNext()) {
                    children.add(in.nextString());
                }
                in.endArray();
                data.children = children;
            } else {
                in.skipValue();
            }
        }
        in.endObject();
        return data;
    }
}
