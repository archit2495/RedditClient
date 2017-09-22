package com.example.architg.redditclientarchit.activity

import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.ImageView
import com.example.architg.redditclientarchit.R
import com.example.architg.redditclientarchit.RedditApplication
import com.example.architg.redditclientarchit.fragments.RecentSearchesFragment
import com.example.architg.redditclientarchit.fragments.SearchTabFragment
import com.example.architg.redditclientarchit.loaders.Loader
import com.example.architg.redditclientarchit.model.QueryChangedEvent
import com.squareup.otto.Bus
import com.squareup.otto.ThreadEnforcer
import java.util.logging.Logger

/**
 * Created by archit.g on 14/09/17.
 */
class SearchActivity : AppCompatActivity(), RecentSearchesFragment.listener {

    val mLoader: Loader = Loader()
    lateinit var editText: EditText
    override fun onSearchStringSelected(query: String) {
        editText.setText("")
        editText.append(query)
        if (query.length > 0) {
            val searchTabFragment = SearchTabFragment.getInstance(editText!!.text.toString())
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_frame, searchTabFragment).commit()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        MainActivity.bus.register(this)
        val recentSearchesFragment = RecentSearchesFragment()
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(R.id.fragment_frame, recentSearchesFragment).commit()
        editText = findViewById(R.id.query)
        val searchImage: ImageView = findViewById(R.id.search_image)
        searchImage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (editText.text != null && editText.text.toString().length > 0) {
                    var fragment: Fragment = supportFragmentManager.findFragmentById(R.id.fragment_frame)
                    if (fragment is RecentSearchesFragment) {
                        val searchTabFragment = SearchTabFragment.getInstance(editText.text.toString())
                        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                        ft.replace(R.id.fragment_frame, searchTabFragment).commit()
                    } else {
                        (application as RedditApplication).updateSearch(editText.text.toString())
                        var event = QueryChangedEvent()
                        event.message = editText.text.toString()
                        MainActivity.bus.post(event)
                    }
                }
            }
        })
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null && p0.length == 0) {
                    var fragment: Fragment = supportFragmentManager.findFragmentById(R.id.fragment_frame)
                    if (fragment is SearchTabFragment) {
                        val recentSearchesFragment = RecentSearchesFragment()
                        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                        ft.replace(R.id.fragment_frame, recentSearchesFragment).commit()
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        val backImageView: ImageView = findViewById(R.id.back_arrow)
        backImageView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                onBackPressed()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.bus.unregister(this)
    }


}