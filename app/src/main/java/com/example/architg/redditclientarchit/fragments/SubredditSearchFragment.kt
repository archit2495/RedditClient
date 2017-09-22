package com.example.architg.redditclientarchit.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.architg.redditclientarchit.R
import com.example.architg.redditclientarchit.activity.MainActivity
import com.example.architg.redditclientarchit.activity.SearchActivity
import com.example.architg.redditclientarchit.loaders.Loader
import com.example.architg.redditclientarchit.model.QueryChangedEvent
import com.example.architg.redditclientarchit.model.SubredditSearchInfo
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
class SubredditSearchFragment : Fragment() {
    var mQuery: String = ""
    lateinit private var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mQuery = arguments.getString("query")
        MainActivity.bus.register(this)
    }
    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val view: View = inflater?.inflate(R.layout.subreddit_search, container, false) ?: return null
        mView = view
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(mQuery != null)
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.bus.unregister(this)
    }
    @Subscribe
    fun query(query: QueryChangedEvent){
        mQuery = query.message
        Logger.getLogger("info")?.info(mQuery + "subscribe")
        loadData()
    }
    fun loadData() {
        Logger.getLogger("info")?.info(mQuery + "loadData")
        val loader: Loader = (activity as SearchActivity).mLoader
        Futures.addCallback(
                loader.loadSubredditSearchInfo(mQuery) as ListenableFuture<SubredditSearchInfo>,
                object : FutureCallback<SubredditSearchInfo> {
                    override fun onSuccess(result: SubredditSearchInfo?) {
                        Handler(Looper.getMainLooper()).post(object : Runnable {
                            override fun run() {
                                val backgroundImageView: ImageView = mView.findViewById(R.id.background)
                                val roundedImageView: CircleImageView = mView.findViewById(R.id.post_image)
                                val headingTextView: TextView = mView.findViewById(R.id.heading)
                                headingTextView.movementMethod = ScrollingMovementMethod()
                                val contentTextView: TextView = mView.findViewById(R.id.content)
                                val subscribersTextView: TextView = mView.findViewById(R.id.subscribers)
                                val onlineTextView: TextView = mView.findViewById(R.id.online)
                                val data: SubredditSearchInfo.Data? = result?.data
                                if(data != null) {
                                    var content: String? = data.description_html
                                    content = content?.replace("&lt;", "<");
                                    content = content?.replace("&gt;", ">");
                                    if (content != null)
                                        contentTextView.setText(Html.fromHtml(content))
                                    val subscribers: Long? = data?.subscribers ?: null
                                    if (subscribers != null)
                                        subscribersTextView.setText(Utils.format(subscribers) + " subscribers")
                                    val activeUsers: Long? = data?.active_user_count ?: null
                                    if (activeUsers != null)
                                        onlineTextView.setText(Utils.format(activeUsers) + " online")
                                    if(data.title != null)
                                    headingTextView.setText(data.title)
                                    if(data.icon_img != null && data.icon_img.isNotEmpty())
                                    Glide.with(activity).load(data.icon_img).into(roundedImageView)
                                    if(data.banner_img != null && data.banner_img.isNotEmpty())
                                    Glide.with(activity).load(data.banner_img).into(backgroundImageView)
                                }
                            }

                        });
                    }

                    override fun onFailure(t: Throwable?) {

                    }

                }
        )
    }

    companion object {
        fun getInstance(query:String): SubredditSearchFragment {
            val subredditSearchFragment = SubredditSearchFragment()
            val bundle = Bundle()
            bundle.putString("query",query)
            subredditSearchFragment.arguments = bundle
            return subredditSearchFragment
        }
    }
}