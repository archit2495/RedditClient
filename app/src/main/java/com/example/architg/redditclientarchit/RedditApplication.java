package com.example.architg.redditclientarchit;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.architg.redditclientarchit.model.Info;
import com.example.architg.redditclientarchit.model.SearchObject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import lombok.extern.java.Log;

/**
 * Created by archit.g on 06/09/17.
 */

public class RedditApplication extends Application {
    SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.getLogger("tyup").info("hello");
        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref",CONTEXT_RESTRICTED);
    }
    public void updateInfo(String mType,Info info){
        Gson gson = new Gson();
        String stringInfo = gson.toJson(info);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Logger.getLogger("tyup").info(mSharedPreferences.getAll().size() + " size");
        editor.putString(mType, stringInfo);
        editor.commit();
    }
    public Info getInfo(String mType){
        Gson gson = new Gson();
        String stringInfo = mSharedPreferences.getString(mType, null);
        Info info = gson.fromJson(stringInfo, Info.class);
        return info;
    }
    public void updateSearch(String query){
        Gson gson = new Gson();
        SearchObject searchObject;
        String searchString = mSharedPreferences.getString("search",null);
        if(searchString == null){
            List<String> searches = new ArrayList<>();
            searches.add(query);
            searchObject = new SearchObject();
            searchObject.searches = searches;
        }else{
            searchObject = gson.fromJson(searchString,SearchObject.class);
            searchObject.searches.add(0,query);
        }
        String searchInfo = gson.toJson(searchObject);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("search",searchInfo);
        editor.commit();
    }
    public List<String> getSearches(){
        Gson gson = new Gson();
        String searchString = mSharedPreferences.getString("search",null);
        if(searchString == null){
            return null;
        }
        SearchObject searchObject = gson.fromJson(searchString,SearchObject.class);
        return searchObject.searches;
    }
}
