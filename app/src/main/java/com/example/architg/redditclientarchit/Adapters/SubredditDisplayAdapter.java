package com.example.architg.redditclientarchit.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.architg.redditclientarchit.R;

import java.util.List;

/**
 * Created by archit.g on 01/09/17.
 */

public class SubredditDisplayAdapter extends  RecyclerView.Adapter<SubredditDisplayAdapter.SubredditViewHolder>{
    private List<String> mSubredditDisplayList;

    public class SubredditViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public SubredditViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.simple_text_view);
        }
    }
    public SubredditDisplayAdapter(List<String> subredditDisplayList){
        mSubredditDisplayList = subredditDisplayList;
    }
    @Override
    public SubredditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subreddit_text_view,parent,false);
        return new SubredditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubredditViewHolder holder, int position) {
        String subreddit = mSubredditDisplayList.get(position);
        holder.textView.setText(subreddit);
    }

    @Override
    public int getItemCount() {
        return mSubredditDisplayList.size();
    }
}
