package com.example.architg.redditclientarchit.model

import com.google.gson.annotations.SerializedName

/**
 * Created by archit.g on 13/09/17.
 */
class SubredditRoot {
    class RootData {
        class SubredditSearchInfo {
            class Data {
                var display_name:String? = null
                var title: String? = null
                var icon_img: String? = null
            }

            @SerializedName("data")
            lateinit var data: Data
        }
        @SerializedName("children")
        lateinit var children: List<SubredditSearchInfo>
    }
    @SerializedName("data")
    lateinit var rootData:RootData
}