package com.example.architg.redditclientarchit.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.architg.redditclientarchit.R
import com.example.architg.redditclientarchit.RedditApplication
import com.example.architg.redditclientarchit.activity.SearchActivity
import java.util.logging.Logger

/**
 * Created by archit.g on 20/09/17.
 */
class RecentSearchesFragment:Fragment(){
    lateinit var mView:View
    lateinit var searches:List<String>
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View? = inflater?.inflate(R.layout.recently_searched_list_view,container,false)?:return null
        mView = view!!
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searches = (activity.application as RedditApplication).searches
        val recentSearchDisplayAdapter = RecentSearchDisplayAdapter(searches)
        val listView = mView.findViewById<ListView>(R.id.search_list)
        listView.adapter = recentSearchDisplayAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val search = searches.get(i)
          //  Logger.getLogger("info")?.info(search + "recent search")
            (activity as SearchActivity).onSearchStringSelected(search)
        }
    }
    class RecentSearchDisplayAdapter(private val mSearchDisplayList: List<String>) : BaseAdapter() {

        override fun getCount(): Int {
            return mSearchDisplayList.size
        }

        override fun getItem(i: Int): String {
            return mSearchDisplayList[i]
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View {
            var convertView = convertView
            if (convertView == null) {
                convertView = LayoutInflater.from(viewGroup.context).inflate(R.layout.search_single_item, viewGroup, false)
            }
            var subreddit = getItem(position)
            (convertView!!.findViewById<View>(R.id.search_text_view) as TextView).text = subreddit
            return convertView
        }
    }
    interface listener{
         fun onSearchStringSelected(query:String)
    }

}