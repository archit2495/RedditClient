package com.example.architg.redditclientarchit.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.example.architg.redditclientarchit.Adapters.SubredditDisplayAdapter;
import com.example.architg.redditclientarchit.Model.SubredditListInfo;
import com.example.architg.redditclientarchit.Network.ApiClient;
import com.example.architg.redditclientarchit.Network.ApiInterface;
import com.example.architg.redditclientarchit.R;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.util.List;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by archit.g on 01/09/17.
 */

public class SubredditFilterFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.recycler_view);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public Future<Boolean> fetch(){
        final SettableFuture<Boolean> future = SettableFuture.create();
        Futures.addCallback((ListenableFuture< SubredditListInfo>)loadData(), new FutureCallback<SubredditListInfo>() {
            @Override
            public void onSuccess(SubredditListInfo result) {
                Dialog dialog = getDialog();
                List<String> subreddits = result.getResponses();
                SubredditDisplayAdapter subredditDisplayAdapter = new SubredditDisplayAdapter(subreddits);
                RecyclerView recyclerView = dialog.findViewById(R.id.subreddit_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                recyclerView.setAdapter(subredditDisplayAdapter);
                future.set(true);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        return future;
    }
    Future<SubredditListInfo> loadData(){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final SettableFuture<SubredditListInfo> future = SettableFuture.create();
        Call<SubredditListInfo> call = apiInterface.getSubredditListInfo();
        call.enqueue(new Callback<SubredditListInfo>() {
            @Override
            public void onResponse(Call<SubredditListInfo> call, Response<SubredditListInfo> response) {
                future.set(response.body());
            }

            @Override
            public void onFailure(Call<SubredditListInfo> call, Throwable t) {
                future.setException(t);
            }
        });
        return future;
    }
}
