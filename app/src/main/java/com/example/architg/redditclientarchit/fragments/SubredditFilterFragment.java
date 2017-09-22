package com.example.architg.redditclientarchit.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.architg.redditclientarchit.R;
import com.example.architg.redditclientarchit.activity.MainActivity;
import com.example.architg.redditclientarchit.adapters.SubredditDisplayAdapter;
import com.example.architg.redditclientarchit.loaders.Loader;
import com.example.architg.redditclientarchit.model.SubredditListInfo;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import static com.example.architg.redditclientarchit.activity.MainActivity.bus;

/**
 * Created by archit.g on 01/09/17.
 */

public class SubredditFilterFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.subreddit_list_view);
        bus.register(this);
        return dialog;
    }
    @Override
    public void onResume(){
        super.onResume();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        getDialog().getWindow().setLayout(width,height);
        loadData();
    }
    void loadData(){
        Loader loader = ((MainActivity)getActivity()).getLoader();
        Futures.addCallback((ListenableFuture<SubredditListInfo>) loader.loadSubredditFilters(), new FutureCallback<SubredditListInfo>() {
            @Override
            public void onSuccess(SubredditListInfo result) {
                Dialog dialog = getDialog();
                final List<String> subreddits = result.getResponses();
                SubredditDisplayAdapter subredditDisplayAdapter = new SubredditDisplayAdapter(subreddits);
                ProgressBar progressBar = dialog.findViewById(R.id.subreddit_progress);
                progressBar.setVisibility(View.GONE);
                ListView listView = dialog.findViewById(R.id.subreddit_list_view);
                listView.setAdapter(subredditDisplayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String subreddit = subreddits.get(i);
                        bus.post(subreddit);
                        SubredditFilterFragment.this.dismiss();
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        /*ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SubredditListInfo> call = apiInterface.getSubredditListInfo();
        call.enqueue(new Callback<SubredditListInfo>() {
            @Override
            public void onResponse(Call<SubredditListInfo> call, Response<SubredditListInfo> response) {
                SubredditListInfo result = response.body();
                Dialog dialog = getDialog();
                final List<String> subreddits = result.getResponses();
                SubredditDisplayAdapter subredditDisplayAdapter = new SubredditDisplayAdapter(subreddits);
                ProgressBar progressBar = dialog.findViewById(R.id.subreddit_progress);
                progressBar.setVisibility(View.GONE);
                ListView listView = dialog.findViewById(R.id.subreddit_list_view);
                listView.setAdapter(subredditDisplayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String subreddit = subreddits.get(i);
                        bus.post(subreddit);
                        SubredditFilterFragment.this.dismiss();
                    }
                });
            }
            @Override
            public void onFailure(Call<SubredditListInfo> call, Throwable t) {

            }
        });*/
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        bus.unregister(this);
    }
    public static SubredditFilterFragment getInstance(){
        return new SubredditFilterFragment();
    }
}
