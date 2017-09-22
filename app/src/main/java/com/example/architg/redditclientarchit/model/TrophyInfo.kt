package com.example.architg.redditclientarchit.model

import com.google.gson.annotations.SerializedName
import lombok.Getter

/**
 * Created by archit.g on 16/09/17.
 */
class TrophyInfo{
    class Data{
        class Children{
            class ChildrenData{
                lateinit var icon_70:String
                lateinit var name:String
            }
            @SerializedName("data")
           lateinit var childrenData:ChildrenData
        }
        @SerializedName("trophies")
        lateinit var trophies:List<Children>
    }
    @Getter
    lateinit var data:Data
}