package com.example.architg.redditclientarchit.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.Post;
import com.example.architg.redditclientarchit.R;

import java.util.List;

/**
 * Created by archit.g on 11/08/17.
 */

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.FeedViewHolder> {
    private List<Info.Data.FeedResponse> mFeedResponseList;
    private Context mContext;
    public  class FeedViewHolder extends RecyclerView.ViewHolder{
        public TextView name,time,heading;
        public ImageView sourceImage,contentImage;
        public FeedViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.name);
            time = view.findViewById(R.id.time);
            heading = view.findViewById(R.id.heading);
            sourceImage = view.findViewById(R.id.sourceImage);
            contentImage = view.findViewById(R.id.contentImage);
        }
    }
    public FeedListAdapter(List<Info.Data.FeedResponse> feedResponseList, Context context){
        mFeedResponseList = feedResponseList;
        mContext = context;
    }
    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_single_item,parent,false);
        return new FeedViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(FeedViewHolder feedViewHolder,int position){
        Info.Data.FeedResponse feedResponse = mFeedResponseList.get(position);
        Post post = feedResponse.getPost();
        feedViewHolder.heading.setText(post.getTitle());
        String time = (String) DateUtils.getRelativeTimeSpanString(post.getCreated());
        feedViewHolder.time.setText(time);
        feedViewHolder.name.setText(post.getSubreddit_name_prefixed());
        loadImage(feedViewHolder.contentImage,post.getPreview().getImageUrl());
       // loadImage(feedViewHolder.sourceImage,feed.getSourceImageUrl());

    }

    @Override
    public int getItemCount() {
        return mFeedResponseList.size();
    }

    private void loadImage(ImageView imageView,String url){
        Glide.with(mContext)
                .load(url)
                .into(imageView);
    }
}
