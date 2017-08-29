package com.example.architg.redditclientarchit.Network;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.SubredditInfo;
import com.google.common.util.concurrent.SettableFuture;
import java.util.concurrent.Future;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by archit.g on 25/08/17.
 */

public class Loader {
    ApiInterface mApiInterface;
    Context mContext;
    public Loader(Context context){
        mContext = context;
    }
    public Future<Info> loadData(String type,String after){
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Info> call = mApiInterface.getInfo(type,after);
        final SettableFuture<Info> future = SettableFuture.create();
        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(@NonNull Call<Info> call, @NonNull Response<Info> response) {
                if(response.body() != null && response.body().getFeedResponse() != null && !response.body().getFeedResponse().isEmpty()){
                    future.set(response.body());
                } else {
                    future.setException(new RuntimeException());
                }
            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                Toast.makeText(mContext,"failed",Toast.LENGTH_LONG).show();
                future.setException(t);
            }
        });
        return future;
    }

    public Future<String> loadSubredditData(String path){
        Call<SubredditInfo> call = mApiInterface.getSubredditInfo(path);
        final SettableFuture<String> future = SettableFuture.create();
        call.enqueue(new Callback<SubredditInfo>() {
            @Override
            public void onResponse(Call<SubredditInfo> call, Response<SubredditInfo> response) {
                String url = response.body().getData().getIcon_img();
                future.set(url);
            }
            @Override
            public void onFailure(Call<SubredditInfo> call, Throwable t) {
                future.setException(t);
            }
        });
        return future;
    }
}
