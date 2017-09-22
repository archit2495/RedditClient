package com.example.architg.redditclientarchit.fragments

import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.architg.redditclientarchit.R
import com.example.architg.redditclientarchit.activity.MainActivity
import com.example.architg.redditclientarchit.loaders.UserInfoLoader
import com.example.architg.redditclientarchit.model.QueryChangedEvent
import com.example.architg.redditclientarchit.model.TrophyInfo
import com.example.architg.redditclientarchit.model.UserInfo
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures.addCallback
import com.google.common.util.concurrent.ListenableFuture
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import de.hdodenhof.circleimageview.CircleImageView
import java.util.logging.Logger

/**
 * Created by archit.g on 16/09/17.
 */
class UserSearchFragment : Fragment() {
    var mQuery = ""
    val mLoader = UserInfoLoader()
    lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mQuery = arguments.getString("query")
        MainActivity.bus.register(this)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater?.inflate(R.layout.user_search, container, false) ?: return null
        mView = view
        return mView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(mQuery != null)
        loadUserData()
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.bus.unregister(this)
    }
    @Subscribe
    fun query(query: QueryChangedEvent){
        Logger.getLogger("info")?.info(mQuery + " userSearch subscribe")
        mQuery = query.message
        loadUserData()
    }
    fun loadUserData() {
        addCallback(mLoader.getUserInfo(mQuery) as ListenableFuture<UserInfo>, object : FutureCallback<UserInfo> {
            override fun onSuccess(result: UserInfo?) {
                if (result != null) {
                    updateUserInfo(result)
                    loadTrophyData()
                }
            }

            override fun onFailure(t: Throwable?) {

            }
        })
    }

    fun updateUserInfo(userInfo: UserInfo) {
        mView.findViewById<TextView>(R.id.user_name).setText(userInfo.data.name)
        val time: String = DateUtils.getRelativeTimeSpanString(userInfo.data.created_utc * 1000L) as String
        mView.findViewById<TextView>(R.id.user_created_utc).setText(time)
        mView.findViewById<TextView>(R.id.karma_count).setText(userInfo.data.comment_karma.toString())
        mView.findViewById<TextView>(R.id.post_count).setText(userInfo.data.link_karma.toString())
    }

    fun loadTrophyData() {
        addCallback(mLoader.getTrophyInfo(mQuery) as ListenableFuture<TrophyInfo>, object : FutureCallback<TrophyInfo> {
            override fun onSuccess(result: TrophyInfo?) {
                if (result != null) {
                    updateTrophyInfo(result)
                }
            }

            override fun onFailure(t: Throwable?) {

            }
        })
    }

    fun updateTrophyInfo(trophyInfo: TrophyInfo) {
        val listView: ListView = mView.findViewById(R.id.trophy_list)
        val trophyListAdapter = TrophyListAdapter(activity, R.layout.trophy_list_single_item, trophyInfo.data.trophies)
        listView.adapter = trophyListAdapter
    }

    class TrophyListAdapter(context: Context, resoureId: Int, items: List<TrophyInfo.Data.Children>) : ArrayAdapter<TrophyInfo.Data.Children>(context, resoureId, items) {
        var mContext = context
        var items = items
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var v: View
            if (convertView == null) {
                var vi: LayoutInflater
                vi = LayoutInflater.from(mContext)
                v = vi.inflate(R.layout.trophy_list_single_item, null)
            } else {
                v = convertView
            }
            var item = items.get(position)
            var imageView: ImageView = v.findViewById(R.id.trophy_image)
            var text: TextView = v.findViewById(R.id.trophy_text)
            text.setText(item.childrenData.name)
            Glide.with(mContext).load(item.childrenData.icon_70).into(imageView)
            return v
        }
    }

    companion object {
        fun getInstance(query:String): UserSearchFragment {
            val userSearchFragment = UserSearchFragment()
            val bundle = Bundle()
            bundle.putString("query",query)
            userSearchFragment.arguments = bundle
            return userSearchFragment
        }
    }
}