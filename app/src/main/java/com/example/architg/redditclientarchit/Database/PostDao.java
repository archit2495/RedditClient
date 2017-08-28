package com.example.architg.redditclientarchit.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.architg.redditclientarchit.Model.RedditDisplayPost;

import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM redditDisplayPost")
    List<RedditDisplayPost> getAll();
    @Insert
    void insertAll(List<RedditDisplayPost> redditDisplayPosts);
}
