package com.example.architg.redditclientarchit.model

/**
 * Created by archit.g on 16/09/17.
 */
class UserInfo{
    class Data{
        lateinit var name:String
        var created_utc:Long = 0
        var comment_karma:Int = 0
        var link_karma:Int = 0
    }
    lateinit var data:Data
}