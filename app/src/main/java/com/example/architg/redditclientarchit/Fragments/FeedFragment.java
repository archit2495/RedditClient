package com.example.architg.redditclientarchit.Fragments;

import android.app.ProgressDialog;
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

import com.example.architg.redditclientarchit.Activity.MainActivity;
import com.example.architg.redditclientarchit.Adapters.RedditPostListAdapter;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Network.Loader;
import com.example.architg.redditclientarchit.R;
import com.example.architg.redditclientarchit.RedditApplication;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.squareup.otto.Subscribe;
import java.util.List;

import static com.example.architg.redditclientarchit.Activity.MainActivity.bus;

public class FeedFragment extends Fragment implements RedditPostListAdapter.FragmentListener {
    String mType;
    String after = "";
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RedditPostListAdapter mRedditPostListAdapter;
    Boolean mIsLoading = false;
    ProgressDialog mProgress;
    Loader mLoader;
    String mSubreddit = "archit";
    SwipeRefreshLayout mSwipeRefreshLayout;
    FragmentManager fragmentManager;
    DotProgressBar mDotProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bus.register(this);
        View view = inflater.inflate(R.layout.feed_list_view, container, false);
        mRedditPostListAdapter = new RedditPostListAdapter(getActivity(), FeedFragment.this);
        mLoader = ((MainActivity)getActivity()).getLoader();
        mProgress = new ProgressDialog(getActivity());
        return view;
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
        mRecyclerView.addOnScrollListener(mScrollListener);
        if (!mLoader.getmSubreddit().equals(mSubreddit)) {
            updateView();
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
        mRedditPostListAdapter.flush();
        after = "";
        loadData();
    }

    @Subscribe
    public void getSubreddit(String subreddit) {
        mSubreddit = subreddit;
        mLoader.setmSubreddit(subreddit);
        updateView();
    }

    public void showImageFragment(String url) {
        ImageDialogFragment imageDialogFragment = ImageDialogFragment.getInstance(url);
        imageDialogFragment.show(fragmentManager, "");
    }

    public void showWebFragment(String url) {
        WebViewFragment webViewFragment = WebViewFragment.getInstance(url);
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
    public void onDestroyView() {
        bus.unregister(this);
        mSubreddit = "archit";
        super.onDestroyView();
    }

    void loadData() {
        mSubreddit = mLoader.getmSubreddit();
        Futures.addCallback((ListenableFuture<Info>) mLoader.loadData(mType, after),
                new FutureCallback<Info>() {
                    @Override
                    public void onSuccess(Info info) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mProgress.dismiss();
                        mIsLoading = false;
                        RedditApplication redditApplication = (RedditApplication)getActivity().getApplicationContext();
                        redditApplication.updateInfo(mType,info);
                        int beginIndex = mRedditPostListAdapter.update(info.getFeedResponse());
                        mDotProgressBar.setVisibility(View.GONE);
                        after = info.getData().getAfter();
                        loadSubredditDataIntoFeed(beginIndex, info.getFeedResponse());
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.i("tmp", t.getStackTrace().getClass().toString());
                        mSwipeRefreshLayout.setRefreshing(false);
                        mProgress.dismiss();
                        mIsLoading = false;
                        RedditApplication redditApplication = (RedditApplication)getActivity().getApplicationContext();
                        mRedditPostListAdapter.update(redditApplication.getInfo(mType).getFeedResponse());
                    }
                });
    }

    private void loadSubredditDataIntoFeed(final int beginIndex, List<Info.Data.FeedResponse> feedResponses) {
        for (int i = 0; i < feedResponses.size(); i++) {
            final Info.Data.FeedResponse feedResponse = feedResponses.get(i);
            final int position = i;
            Futures.addCallback((ListenableFuture<String>) mLoader.loadSubredditData(feedResponse.getPost().getSubreddit_name_prefixed().substring(2)), new FutureCallback<String>() {
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

    public static FeedFragment getInstance(String type) {
        FeedFragment feedFragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        feedFragment.setArguments(bundle);
        return feedFragment;
    }

}
