package com.example.architg.redditclientarchit.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.architg.redditclientarchit.Adapters.RedditPostListAdapter;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.RedditDisplayPost;
import com.example.architg.redditclientarchit.Network.Loader;
import com.example.architg.redditclientarchit.R;
import com.example.architg.redditclientarchit.Utility.Utils;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

/**
 * Created by archit.g on 28/08/17.
 */

public class FeedFragment extends Fragment implements RedditPostListAdapter.FragmentListener{
    Context mContext;
    String mType;
    String after = "";
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RedditPostListAdapter mRedditPostListAdapter;
    Boolean mIsLoading = false;
    ProgressDialog mProgress;
    Loader mLoader;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FragmentManager fragmentManager;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipe_refresh_recycler_view, container, false);
        mRecyclerView = view.findViewById(R.id.feed_recycler_view);
        mLayoutManager = new LinearLayoutManager(mContext);
        mProgress = new ProgressDialog(mContext);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRedditPostListAdapter = new RedditPostListAdapter(mContext,FeedFragment.this);
        mRecyclerView.setAdapter(mRedditPostListAdapter);
        fragmentManager = getActivity().getSupportFragmentManager();
        mLoader = new Loader(mContext);
        RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isPageBeingLoaded()) {
                    mProgress.show();
                    mIsLoading = true;
                    loadData();
                }
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);
        loadData();
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mRedditPostListAdapter.flush();
                        after = "";
                        loadData();
                    }
                }
        );

    }

    public void showDialogFragment(String url){
        Log.i("clicked",url);
        ImageDialogFragment dialogFragment = new ImageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentManager,"");
    }

    @Override
    public void showWebFragment(String url) {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        webViewFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        webViewFragment.show(fragmentManager,"");
    }

    Boolean isPageBeingLoaded() {
        int totalItemCount = mRedditPostListAdapter.getListSize();
        int pastVisibleItems = mLayoutManager.findLastVisibleItemPosition();
        if (pastVisibleItems == totalItemCount - 1 && !mIsLoading) {
            return true;
        }
        return false;
    }

    void loadData() {
        Futures.addCallback((ListenableFuture<Info>) mLoader.loadData(mType,after),
                new FutureCallback<Info>() {
                    @Override
                    public void onSuccess(Info info) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mProgress.dismiss();
                        mIsLoading = false;
                        List<RedditDisplayPost> redditDisplayPosts = Utils.convertFeedResposeListToRedditDisplayPostList(mContext, info.getFeedResponse());
                        int beginIndex = mRedditPostListAdapter.update(redditDisplayPosts);
                        after = info.getData().getAfter();
                        loadSubredditDataIntoFeed(beginIndex,redditDisplayPosts);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.i("tmp",t.getStackTrace().getClass().toString());
                    }
                });
    }
    private void loadSubredditDataIntoFeed(final int beginIndex,List<RedditDisplayPost> redditDisplayPosts){
        for(int i = 0;i < redditDisplayPosts.size();i++){
            final RedditDisplayPost redditDisplayPost = redditDisplayPosts.get(i);
            final int position = i;
            Futures.addCallback((ListenableFuture<String>) mLoader.loadSubredditData(redditDisplayPost.getName().substring(2)), new FutureCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    mRedditPostListAdapter.updateSourceImage(result,beginIndex + position);
                }

                @Override
                public void onFailure(Throwable t) {
                    mRedditPostListAdapter.updateSourceImage(null,beginIndex + position);
                }
            });
        }

    }
}
