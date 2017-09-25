package com.example.architg.redditclientarchit.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.architg.redditclientarchit.R;
import com.example.architg.redditclientarchit.RedditApplication;
import com.example.architg.redditclientarchit.activity.MainActivity;
import com.example.architg.redditclientarchit.activity.SearchActivity;
import com.example.architg.redditclientarchit.adapters.RedditPostListAdapter;
import com.example.architg.redditclientarchit.loaders.Loader;
import com.example.architg.redditclientarchit.model.Info;
import com.github.clans.fab.FloatingActionButton;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.squareup.otto.Subscribe;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.logging.Logger;

import static com.example.architg.redditclientarchit.activity.MainActivity.bus;

public class FeedFragment extends Fragment implements RedditPostListAdapter.FragmentListener,MainActivity.SubredditChangeListener{
    String mType;
    String after = "";
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RedditPostListAdapter mRedditPostListAdapter;
    Boolean mIsLoading = false;
    ProgressDialog mProgress;
    Loader mLoader;
    SwipeRefreshLayout mSwipeRefreshLayout;
    DotProgressBar mDotProgressBar;
    LinearLayout errorLayout;
    View mView;
    LinearLayout noResults;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.feed_list_view, container, false);
        mRedditPostListAdapter = new RedditPostListAdapter(getActivity(), FeedFragment.this);
        mLoader = ((MainActivity)getActivity()).getLoader();
        mProgress = new ProgressDialog(getActivity());
        errorLayout = mView.findViewById(R.id.error);
        errorLayout.setVisibility(View.GONE);
        noResults = mView.findViewById(R.id.no_results);
        noResults.setVisibility(View.GONE);
        ((MainActivity)getActivity()).register(this);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.feed_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mDotProgressBar = view.findViewById(R.id.dot_progress_bar);
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRedditPostListAdapter);
        RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isPageBeingLoaded() && (after != null) && (!after.equals(""))){
                    mProgress.show();
                    mIsLoading = true;
                    loadData();
                }
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);
     //   updateView();
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        errorLayout.setVisibility(View.GONE);
                        updateView();
                    }
                }
        );

    }

    public void updateView() {
        noResults.setVisibility(View.GONE);
        mRedditPostListAdapter.flush();
        after = "";
        Logger.getLogger("gtyu").info("in update view");
        loadData();
    }

    public void showImageFragment(String url) {
        ImageDialogFragment imageDialogFragment = ImageDialogFragment.getInstance(url);
        imageDialogFragment.show(getActivity().getSupportFragmentManager(), "");
    }

    public void showWebFragment(String url) {
        WebViewFragment webViewFragment = WebViewFragment.getInstance(url);
        webViewFragment.show(getActivity().getSupportFragmentManager(), "");
    }

    Boolean isPageBeingLoaded() {
        int totalItemCount = mRedditPostListAdapter.getItemCount();
        int pastVisibleItems = mLayoutManager.findLastVisibleItemPosition();
        if (pastVisibleItems == totalItemCount - 1 && !mIsLoading) {
            return true;
        }
        return false;
    }
    void loadData() {
        Logger.getLogger("gty").info("load data " + mType + " type " + after + " after " + mLoader.getmSubreddit() + " subreddit");
        Futures.addCallback((ListenableFuture<Info>) mLoader.loadData(mType, after),
                new FutureCallback<Info>() {
                    @Override
                    public void onSuccess(Info info) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mProgress.dismiss();
                        mIsLoading = false;
                        if(info.getFeedResponse() == null || info.getFeedResponse().size() == 0){
                            noResults.setVisibility(View.VISIBLE);
                            mDotProgressBar.setVisibility(View.GONE);
                        }else {
                            noResults.setVisibility(View.GONE);
                            RedditApplication redditApplication = (RedditApplication) getActivity().getApplicationContext();
                            redditApplication.updateInfo(mLoader.getmSubreddit() + "/" + mType, info);
                            Logger.getLogger("hgbn").info("adding " + mLoader.getmSubreddit() + "/" + mType);
                            mDotProgressBar.setVisibility(View.GONE);
                            mRedditPostListAdapter.update(info.getFeedResponse());
                            after = info.getData().getAfter();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Logger.getLogger("jjfv").info(t.getMessage());
                        mSwipeRefreshLayout.setRefreshing(false);
                        mProgress.dismiss();
                        mIsLoading = false;
                        RedditApplication redditApplication = (RedditApplication)getActivity().getApplicationContext();
                        mDotProgressBar.setVisibility(View.GONE);
                        Logger.getLogger("hgbn").info("retrieving " + mLoader.getmSubreddit() + "/" + mType);
                        Info info = redditApplication.getInfo(mLoader.getmSubreddit() + "/" + mType);
                        if(info == null){
                            Logger.getLogger("hgbn").info("info null" + mLoader.getmSubreddit() + "/" + mType);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.VISIBLE);
                        }else {
                            mRedditPostListAdapter.update(info.getFeedResponse());
                        }
                    }
                });
    }

    public static FeedFragment getInstance(String type) {
        FeedFragment feedFragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        feedFragment.setArguments(bundle);
        return feedFragment;
    }

}
