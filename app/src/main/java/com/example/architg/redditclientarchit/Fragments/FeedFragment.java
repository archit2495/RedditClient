package com.example.architg.redditclientarchit.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.util.List;

import static com.example.architg.redditclientarchit.activity.MainActivity.bus;

public class FeedFragment extends Fragment implements RedditPostListAdapter.FragmentListener {
    Context mContext;
    String mSubreddit = "archit";
    String mType="cg";
    String after = "";
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RedditPostListAdapter mRedditPostListAdapter;
    Boolean mIsLoading = false;
    ProgressDialog mProgress;
    Loader mLoader;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FragmentManager fragmentManager;
    SharedPreferences mSharedPreferences;
    boolean subredditSelected = false;
    boolean initDone = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(mType, "onCreate");
        mSharedPreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        mType = getArguments().getString("type");
        //mSubreddit = getArguments().getString("subreddit");
        // loadData();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(mType, "onCreateView");
        bus.register(this);
        View view = inflater.inflate(R.layout.swipe_refresh_recycler_view, container, false);
        mRedditPostListAdapter = new RedditPostListAdapter(mContext, FeedFragment.this);
        mLoader = new Loader(getActivity().getApplicationContext());
        mProgress = new ProgressDialog(mContext);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(mType, "onViewCreated");
        mRecyclerView = view.findViewById(R.id.feed_recycler_view);
        mLayoutManager = new LinearLayoutManager(mContext);
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRedditPostListAdapter);
        fragmentManager = getActivity().getSupportFragmentManager();
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
        initDone = true;
        mRecyclerView.addOnScrollListener(mScrollListener);
        Log.i(mType + " hgf",mSubreddit);
        if(!mSharedPreferences.getString("subreddit","").equals(mSubreddit)) {
            updateView();
            Log.i(mType + "here" + mSharedPreferences.getString("subreddit",""),"called + mSubreddit");
        }
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        updateView();
                    }
                }
        );

    }

    public void updateView() {
        Log.i(mType, "updateView");
        mRedditPostListAdapter.flush();
        after = "";
        boolean check = isVisible();
        Log.i(mType,"visibility" + check);
       // if (isVisible()) {
            loadData();
       // }
    }

    @Subscribe
    public void getSubreddit(String subreddit) {
        Log.d("onSubReditChanged/" +mType, "getSubreddit:"+subreddit);
        mSubreddit = subreddit;
        subredditSelected = true;
        updateSharedPref();
        updateView();
    }

    void updateSharedPref() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("subreddit", mSubreddit);
        editor.commit();
    }

    public void showImageFragment(String url) {
        Log.i("clicked", url);
        ImageDialogFragment imageDialogFragment = ImageDialogFragment.getInstance(url);
        imageDialogFragment.show(fragmentManager, "");
    }

    public void showWebFragment(String url) {
        WebViewFragment webViewFragment = WebViewFragment.getInstance("url");
        webViewFragment.show(fragmentManager, "");
    }

    Boolean isPageBeingLoaded() {
        int totalItemCount = mRedditPostListAdapter.getListSize();
        int pastVisibleItems = mLayoutManager.findLastVisibleItemPosition();
        if (pastVisibleItems == totalItemCount - 1 && !mIsLoading) {
            return true;
        }
        return false;
    }
    @Override
    public void onDestroyView(){
        Log.i(mType, "onDestroyView");
        bus.unregister(this);
        mSubreddit = "archit";
        super.onDestroyView();
    }
    void loadData() {
        Log.i(mType, "loadData");
        mSubreddit = mSharedPreferences.getString("subreddit", "");
        Futures.addCallback((ListenableFuture<Info>) mLoader.loadData(mSubreddit, mType, after),
                new FutureCallback<Info>() {
                    @Override
                    public void onSuccess(Info info) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mProgress.dismiss();
                        mIsLoading = false;
                        Gson gson = new Gson();
                        String stringInfo = gson.toJson(info);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString(mType, stringInfo);
                        editor.commit();
                        List<RedditDisplayPost> redditDisplayPosts = Utils.convertFeedResposeListToRedditDisplayPostList(mContext, info.getFeedResponse());
                        int beginIndex = mRedditPostListAdapter.update(redditDisplayPosts);
                        after = info.getData().getAfter();
                        loadSubredditDataIntoFeed(beginIndex, redditDisplayPosts);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.i("tmp", t.getStackTrace().getClass().toString());
                        mSwipeRefreshLayout.setRefreshing(false);
                        mProgress.dismiss();
                        mIsLoading = false;
                        Gson gson = new Gson();
                        String stringInfo = mSharedPreferences.getString(mType, null);
                        Info info = gson.fromJson(stringInfo, Info.class);
                        List<RedditDisplayPost> redditDisplayPosts = Utils.convertFeedResposeListToRedditDisplayPostList(mContext, info.getFeedResponse());
                        mRedditPostListAdapter.update(redditDisplayPosts);
                    }
                });
    }

    private void loadSubredditDataIntoFeed(final int beginIndex, List<RedditDisplayPost> redditDisplayPosts) {
        for (int i = 0; i < redditDisplayPosts.size(); i++) {
            final RedditDisplayPost redditDisplayPost = redditDisplayPosts.get(i);
            final int position = i;
            Futures.addCallback((ListenableFuture<String>) mLoader.loadSubredditData(redditDisplayPost.getName().substring(2)), new FutureCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    mRedditPostListAdapter.updateSourceImage(result, beginIndex + position);
                }

                @Override
                public void onFailure(Throwable t) {
                    mRedditPostListAdapter.updateSourceImage(null, beginIndex + position);
                }
            });
        }

    }

    public static FeedFragment getInstance(String type, String subreddit) {
        Log.i(type, "getInstance");
        FeedFragment feedFragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("subreddit", subreddit);
        feedFragment.setArguments(bundle);
        return feedFragment;
    }

}
