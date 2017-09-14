package com.example.architg.redditclientarchit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.architg.redditclientarchit.R;

import java.util.List;

/**
 * Created by archit.g on 01/09/17.
 */

public class SubredditDisplayAdapter extends BaseAdapter{
    private List<String> mSubredditDisplayList;

    @Override
    public int getCount() {
        return mSubredditDisplayList.size();
    }

    @Override
    public String getItem(int i) {
        return mSubredditDisplayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subreddit_text_view,viewGroup,false);
        }
        String subreddit = getItem(position);
        subreddit = subreddit.substring(2);
        ((TextView)convertView.findViewById(R.id.simple_text_view)).setText(subreddit);
        return convertView;
    }

    public SubredditDisplayAdapter(List<String> subredditDisplayList){
        mSubredditDisplayList = subredditDisplayList;
    }
}
