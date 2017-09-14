package com.example.architg.redditclientarchit.model

import com.google.gson.annotations.SerializedName

/**
 * Created by archit.g on 13/09/17.
 */
class SubredditSearchInfo{
     class Data{
        val description_html:String = ""
        val title:String = "";
        val icon_img:String = ""
        val active_user_count:String = ""
        val subscribers:String = ""
    }
    @SerializedName("data")
    val data:Data = Data()
}