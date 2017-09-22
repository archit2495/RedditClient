package com.example.architg.redditclientarchit.loaders

import com.example.architg.redditclientarchit.model.TrophyInfo
import com.example.architg.redditclientarchit.model.UserInfo
import com.example.architg.redditclientarchit.network.ApiClient
import com.example.architg.redditclientarchit.network.ApiInterface
import com.google.common.util.concurrent.SettableFuture
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Future

/**
 * Created by archit.g on 17/09/17.
 */
class UserInfoLoader {
    var mApiInterface: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
    fun getUserInfo(query: String): Future<UserInfo> {
        val call: Call<UserInfo> = mApiInterface.getUserInfo(query)
        val future: SettableFuture<UserInfo> = SettableFuture.create()
        call.enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>?, response: Response<UserInfo>?) {
                future.set(response?.body())
            }

            override fun onFailure(call: Call<UserInfo>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        return future
    }
    fun getTrophyInfo(query: String): Future<TrophyInfo> {
        val call: Call<TrophyInfo> = mApiInterface.getTrophyInfo(query)
        val future: SettableFuture<TrophyInfo> = SettableFuture.create()
        call.enqueue(object : Callback<TrophyInfo> {
            override fun onResponse(call: Call<TrophyInfo>?, response: Response<TrophyInfo>?) {
                future.set(response?.body())
            }

            override fun onFailure(call: Call<TrophyInfo>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        return future
    }
}