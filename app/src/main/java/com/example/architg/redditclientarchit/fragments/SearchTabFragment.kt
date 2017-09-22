package com.example.architg.redditclientarchit.fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.architg.redditclientarchit.R

/**
 * Created by archit.g on 21/09/17.
 */
class SearchTabFragment:Fragment(){
    lateinit var mQuery:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mQuery = arguments.getString("query")
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View? = inflater?.inflate(R.layout.search_tab_layout,container,false)?:return null
        return view
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = view?.findViewById<TabLayout>(R.id.tabs)
        val viewPager = view?.findViewById<ViewPager>(R.id.viewpager)
        val array = arrayOf("Posts","Subreddit","Users")
        val adapter = ViewPagerAdapter(childFragmentManager,array,mQuery)
        viewPager?.adapter = adapter
        tabLayout?.setupWithViewPager(viewPager)
        viewPager?.offscreenPageLimit = 3
    }
    companion object {
        fun getInstance(query:String):SearchTabFragment{
            val searchTabFragment = SearchTabFragment()
            val bundle = Bundle()
            bundle.putString("query",query)
            searchTabFragment.arguments = bundle
            return searchTabFragment
        }
    }
    class ViewPagerAdapter(manager: FragmentManager, values:Array<String>,mQuery:String): FragmentPagerAdapter(manager){
        var categories:Array<String> = values
        val mQuery = mQuery
        override fun getItem(position: Int): Fragment {
            val type = categories[position]
            if(type.equals("Posts")){
                return PostSearchFragment.getInstance(mQuery,"top")
            }else if(type.equals("Subreddit")){
                return SubredditSearchFragment.getInstance(mQuery)
            }else {
                return UserSearchFragment.getInstance(mQuery)
            }
        }

        override  fun getPageTitle(position: Int): CharSequence {
            return categories[position]
        }
        override fun getCount(): Int {
            return categories.size
        }

    }
}