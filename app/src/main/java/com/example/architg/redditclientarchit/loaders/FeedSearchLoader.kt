package com.example.architg.redditclientarchit.loaders

import com.example.architg.redditclientarchit.model.Info
import com.example.architg.redditclientarchit.network.ApiClient
import com.example.architg.redditclientarchit.network.ApiInterface
import com.google.common.util.concurrent.SettableFuture
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Future

/**
 * Created by archit.g on 18/09/17.
 */
class FeedSearchLoader{
    val mApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
    fun loadSearchFeed(query:String,sortType:String,after:String):Future<Info>{
        var call = mApiInterface.getFeedSearchInfo(query,sortType,after)
        var future:SettableFuture<Info> = SettableFuture.create()
        call.enqueue(object :Callback<Info>{
            override fun onResponse(call: Call<Info>?, response: Response<Info>?) {
                future.set(response?.body())
            }
            override fun onFailure(call: Call<Info>?, t: Throwable?) {

            }
        })
        return future;
    }
}