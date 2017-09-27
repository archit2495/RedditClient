package com.example.architg.redditclientarchit.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.architg.redditclientarchit.R
import com.example.architg.redditclientarchit.activity.MainActivity
import com.example.architg.redditclientarchit.activity.SearchActivity
import com.example.architg.redditclientarchit.loaders.Loader
import com.example.architg.redditclientarchit.model.QueryChangedEvent
import com.example.architg.redditclientarchit.model.SubredditInfo
import com.example.architg.redditclientarchit.model.SubredditRoot
import com.example.architg.redditclientarchit.utility.SubredditClickListener
import com.example.architg.redditclientarchit.utility.Utils
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import de.hdodenhof.circleimageview.CircleImageView
import java.util.logging.Logger

/**
 * Created by archit.g on 14/09/17.
 */
class SubredditSearchFragment : Fragment(), SubredditClickListener {
    var mQuery: String = ""
    lateinit private var mView: View
    lateinit var subredditSearchFragment: SubredditClickListener
    lateinit var recyclerView: RecyclerView
    lateinit var noResults: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mQuery = arguments.getString("query")
        MainActivity.bus.register(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val view: View = inflater?.inflate(R.layout.subreddit_search, container, false) ?: return null
        mView = view
        recyclerView = mView.findViewById(R.id.subreddit_recycler_view)
        noResults = mView.findViewById(R.id.no_results)
        subredditSearchFragment = this
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mQuery != null)
            loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.bus.unregister(this)
    }

    @Subscribe
    fun query(query: QueryChangedEvent) {
        mQuery = query.message
        Logger.getLogger("info")?.info(mQuery + "subscribe")
        loadData()
    }

    fun loadData() {
        recyclerView.visibility = View.VISIBLE
        noResults.visibility = View.GONE
        Logger.getLogger("info")?.info(mQuery + "loadData")
        val loader: Loader = (activity as SearchActivity).mLoader
        Futures.addCallback(
                loader.loadSubredditSearchInfo(mQuery) as ListenableFuture<SubredditRoot>,
                object : FutureCallback<SubredditRoot> {
                    override fun onSuccess(result: SubredditRoot?) {
                        if (result != null && result.rootData.children.size > 0) {
                            val adapter = SubredditSearchAdapter(result.rootData.children, activity.applicationContext, subredditSearchFragment)
                            recyclerView.layoutManager = LinearLayoutManager(activity)
                            recyclerView.adapter = adapter
                        } else {
                            recyclerView.visibility = View.GONE
                            noResults.visibility = View.VISIBLE
                        }

                    }

                    override fun onFailure(t: Throwable?) {

                    }

                }
        )
    }

    override fun subredditClicked(subreddit: String?) {
        if (subreddit == null) {
            return
        }
        val intent = Intent(activity, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("subreddit", "r/" + subreddit)
        activity.startActivity(intent)
        activity.finish()
    }

    companion object {
        fun getInstance(query: String): SubredditSearchFragment {
            val subredditSearchFragment = SubredditSearchFragment()
            val bundle = Bundle()
            bundle.putString("query", query)
            subredditSearchFragment.arguments = bundle
            return subredditSearchFragment
        }
    }

    class SubredditSearchAdapter(subreddits: List<SubredditRoot.RootData.SubredditSearchInfo>, mContext: Context, listener: SubredditClickListener) : RecyclerView.Adapter<SubredditSearchAdapter.SubredditViewHolder>() {
        val mSubreddits = subreddits
        val mContext = mContext
        val subredditClickListener = listener

        class SubredditViewHolder : RecyclerView.ViewHolder {
            var titleTextView: TextView
            var headingTextView: TextView
            var iconImageView: CircleImageView
            var listItem: View

            constructor(itemView: View) : super(itemView) {
                listItem = itemView
                titleTextView = itemView.findViewById(R.id.title)
                headingTextView = itemView.findViewById(R.id.description)
                iconImageView = itemView.findViewById(R.id.icon)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SubredditViewHolder {
            val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.subreddit_search_single_item, parent, false)
            return SubredditViewHolder(view)
        }

        override fun onBindViewHolder(holder: SubredditViewHolder?, position: Int) {
            val subreddit = mSubreddits.get(position)
            holder?.titleTextView?.setText(subreddit.data.display_name)
            holder?.headingTextView?.setText(subreddit.data.title)
            val requestOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(mContext.getDrawable(R.mipmap.default_icon))
            Glide.with(mContext).load(subreddit.data.icon_img).apply(requestOptions).into(holder?.iconImageView)
            holder?.itemView?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    subredditClickListener.subredditClicked(subreddit.data.display_name)
                }
            })

        }

        override fun getItemCount(): Int {
            return mSubreddits.size
        }

    }


}