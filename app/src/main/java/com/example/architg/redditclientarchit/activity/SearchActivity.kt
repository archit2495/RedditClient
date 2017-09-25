package com.example.architg.redditclientarchit.activity

import android.content.Context
import android.graphics.Color
import android.media.Image
import android.opengl.Visibility
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
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.RenderProcessGoneDetail
import android.widget.ImageView
import android.widget.TextView
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
    fun clickEvent(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        if (editText.text != null && editText.text.toString().length > 0) {
            (application as RedditApplication).updateSearch(editText.text.toString())
            var fragment: Fragment = supportFragmentManager.findFragmentById(R.id.fragment_frame)
            if (fragment is RecentSearchesFragment) {
                val searchTabFragment = SearchTabFragment.getInstance(editText.text.toString())
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                ft.replace(R.id.fragment_frame, searchTabFragment).commit()
            } else {
                var event = QueryChangedEvent()
                event.message = editText.text.toString()
                MainActivity.bus.post(event)
            }
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
        val clearImage:ImageView = findViewById(R.id.clear)
        clearImage.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                editText.text = Editable.Factory.getInstance().newEditable("")
            }
        })
        searchImage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
               clickEvent()
            }
        })
        editText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                clickEvent()
                return@OnEditorActionListener true
            }
            false
        })

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null && p0.length == 0) {
                    clearImage.visibility = View.GONE
                    var fragment: Fragment = supportFragmentManager.findFragmentById(R.id.fragment_frame)
                    if (fragment is SearchTabFragment) {
                        val recentSearchesFragment = RecentSearchesFragment()
                        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                        ft.replace(R.id.fragment_frame, recentSearchesFragment).commit()
                    }
                }else if(p0 != null){
                    clearImage.visibility = View.VISIBLE
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