package com.example.architg.redditclientarchit.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.architg.redditclientarchit.R
import com.example.architg.redditclientarchit.activity.MainActivity
import com.example.architg.redditclientarchit.adapters.RedditPostListAdapter
import com.example.architg.redditclientarchit.loaders.FeedSearchLoader
import com.example.architg.redditclientarchit.model.Info
import com.example.architg.redditclientarchit.model.QueryChangedEvent
import com.github.silvestrpredko.dotprogressbar.DotProgressBar
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import java.util.logging.Logger

/**
 * Created by archit.g on 18/09/17.
 */
class PostSearchFragment : Fragment(), RedditPostListAdapter.FragmentListener {
    lateinit var mType: String
    var mQuery: String = "funny"
    var after: String = ""
    lateinit var mRecyclerView: RecyclerView
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRedditPostListAdapter: RedditPostListAdapter
    var mIsLoading: Boolean? = false
    lateinit var mProgress: ProgressDialog
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    lateinit var mDotProgressBar: DotProgressBar
    lateinit var errorTextView: TextView
    lateinit var mView: View
    lateinit var noResults:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mType = arguments.getString("type")
        mQuery = arguments.getString("query")
        MainActivity.bus.register(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.feed_list_view, container, false)!!
        mRedditPostListAdapter = RedditPostListAdapter(activity, this)
        mProgress = ProgressDialog(activity)
    //    errorTextView = mView.findViewById(R.id.error)
    //    errorTextView.visibility = View.GONE
        return mView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mRecyclerView = view!!.findViewById(R.id.feed_recycler_view)
        mLayoutManager = LinearLayoutManager(activity)
        mDotProgressBar = view.findViewById(R.id.dot_progress_bar)
        noResults = view.findViewById(R.id.no_results)
        mProgress.setMessage("Please wait...")
        mProgress.setCancelable(false)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mRedditPostListAdapter
        val mScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isPageBeingLoaded()!!) {
                    mProgress.show()
                    mIsLoading = true
                    loadData()
                }
            }
        }
        mRecyclerView.addOnScrollListener(mScrollListener)
        if(mQuery != null)
        loadData()
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh)
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        mSwipeRefreshLayout.setOnRefreshListener { updateView() }
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.bus.unregister(this)
    }
    @Subscribe
    fun query(query: QueryChangedEvent){
        Logger.getLogger("info")?.info(query.message + "post subscribe")
        mQuery = query.message
        updateView()
    }
    fun updateView() {
        mRedditPostListAdapter.flush()
        after = ""
        loadData()
    }

    internal fun loadData() {
        noResults.visibility = View.GONE
        Logger.getLogger("info")?.info(mQuery + " loadData")
        val loader = FeedSearchLoader()
        Futures.addCallback(loader.loadSearchFeed(mQuery, mType, after) as ListenableFuture<Info>,
                object : FutureCallback<Info> {
                    override fun onSuccess(info: Info?) {
                        if(info == null || info.feedResponse == null || info.feedResponse.size == 0){
                            noResults.visibility = View.VISIBLE
                            mDotProgressBar.visibility = View.GONE
                        }else {
                            mSwipeRefreshLayout.isRefreshing = false
                            mProgress.dismiss()
                            mIsLoading = false
                            mDotProgressBar.visibility = View.GONE
                            mRedditPostListAdapter.update(info.feedResponse)
                            after = info.data.after
                        }
                    }

                    override fun onFailure(t: Throwable) {
                        mSwipeRefreshLayout.isRefreshing = false
                        mProgress.dismiss()
                        mIsLoading = false
                        mDotProgressBar.visibility = View.GONE
                        mSwipeRefreshLayout.visibility = View.GONE
                       // errorTextView.visibility = View.VISIBLE

                    }
                })
    }

    internal fun isPageBeingLoaded(): Boolean? {
        val totalItemCount = mRedditPostListAdapter.itemCount
        val pastVisibleItems = mLayoutManager.findLastVisibleItemPosition()
        return pastVisibleItems == totalItemCount - 1 && mIsLoading == false
    }

    override fun showImageFragment(url: String) {
        val imageDialogFragment = ImageDialogFragment.getInstance(url)
        imageDialogFragment.show(activity.supportFragmentManager, "")
    }

    override fun showWebFragment(url: String) {
        val webViewFragment = WebViewFragment.getInstance(url)
        webViewFragment.show(activity.supportFragmentManager, "")
    }

    companion object {
        fun getInstance(query:String,type: String): PostSearchFragment {
            val feedSearchFragment = PostSearchFragment()
            val bundle = Bundle()
            bundle.putString("type", type)
            bundle.putString("query",query)
            feedSearchFragment.arguments = bundle
            return feedSearchFragment
        }
    }

}