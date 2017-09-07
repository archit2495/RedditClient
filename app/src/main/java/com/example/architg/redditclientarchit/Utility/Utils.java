package com.example.architg.redditclientarchit.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class Utils {
    static SharedPreferences mSharedPreferences;
    public static void initUtils(Activity activity){
        mSharedPreferences = activity.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    }
}
