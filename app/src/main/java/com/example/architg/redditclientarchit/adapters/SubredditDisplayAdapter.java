package com.example.architg.redditclientarchit.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.architg.redditclientarchit.R;

import java.util.List;

/**
 * Created by archit.g on 01/09/17.
 */

public class SubredditDisplayAdapter extends ArrayAdapter {
    private List<String> mSubreddits;
    private Context mContext;

    public SubredditDisplayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> subreddits) {
        super(context, resource, subreddits);
        mSubreddits = subreddits;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = super.getView(position, convertView, parent);
            ((TextView) v.findViewById(R.id.text1)).setHint((String) getItem(1)); //"Hint to be displayed"
            ((TextView) v.findViewById(R.id.text1)).setHintTextColor(Color.parseColor("#ffffff"));

        return v;
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        //  LayoutInflater inflater = getLayoutInflater();
        View spinnerItem;
        if (position == 0) {
            spinnerItem = LayoutInflater.from(mContext).inflate(R.layout.enter_subreddit_single_item_text_view, null);
            ((TextView) spinnerItem.findViewById(R.id.text1)).setText(mSubreddits.get(position));

        } else {
            spinnerItem = LayoutInflater.from(mContext).inflate(R.layout.simple_text_view, null);
            ((TextView) spinnerItem).setText(mSubreddits.get(position));
        }
        return spinnerItem;
    }

    @Override
    public int getCount() {
        return mSubreddits.size();
    }

    public void update(List<String> fetchedSubreddits) {
        mSubreddits.addAll(fetchedSubreddits);
        notifyDataSetChanged();
    }

    public void updateLastItem(String subreddit) {
        mSubreddits.add(subreddit);
        notifyDataSetChanged();
    }
}
