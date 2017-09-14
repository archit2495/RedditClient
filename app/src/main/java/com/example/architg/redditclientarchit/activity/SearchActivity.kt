package com.example.architg.redditclientarchit.activity

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import lombok.extern.java.Log
import java.util.logging.Logger

/**
 * Created by archit.g on 14/09/17.
 */
class SearchActivity:AppCompatActivity(){
    var mType:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onSearchRequested()
        val intent: Intent = getIntent()
        if(Intent.ACTION_SEARCH.equals(intent.action)){
            val query:String = intent.getStringExtra(SearchManager.QUERY)
          //  mType = intent.getStringExtra("type")
            doMySearch(query)
        }
    }

    fun doMySearch(query:String){
        val log = Logger.getLogger("archit");
        log.info(mType + " this " + query)
    }
}