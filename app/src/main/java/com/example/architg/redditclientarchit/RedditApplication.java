package com.example.architg.redditclientarchit;

import android.app.Application;
import android.content.SharedPreferences;
import com.example.architg.redditclientarchit.model.Info;
import com.google.gson.Gson;

/**
 * Created by archit.g on 06/09/17.
 */

public class RedditApplication extends Application {
    SharedPreferences mSharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref",CONTEXT_RESTRICTED);
    }
    public void updateInfo(String mType,Info info){
        Gson gson = new Gson();
        String stringInfo = gson.toJson(info);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(mType, stringInfo);
        editor.commit();
    }
    public Info getInfo(String mType){
        Gson gson = new Gson();
        String stringInfo = mSharedPreferences.getString(mType, null);
        Info info = gson.fromJson(stringInfo, Info.class);
        return info;
    }
}
