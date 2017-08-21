package com.example.architg.redditclientarchit.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.Model.Post;
import com.example.architg.redditclientarchit.R;

import java.util.List;

import static com.example.architg.redditclientarchit.R.mipmap.ic_launcher;

/**
 * Created by archit.g on 11/08/17.
 */

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.FeedViewHolder> {
    private List<Info.Data.FeedResponse> mFeedResponseList;
    private Context mContext;
    public  class FeedViewHolder extends RecyclerView.ViewHolder{
        public TextView name,time,heading,contentText;
        public ImageView sourceImage,contentImage;
        public FeedViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.name);
            time = view.findViewById(R.id.time);
            heading = view.findViewById(R.id.heading);
            sourceImage = view.findViewById(R.id.sourceImage);
            contentImage = view.findViewById(R.id.contentImage);
            contentText = view.findViewById(R.id.contentText);
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
       // Log.i("archit",post.getSubreddit_name_prefixed());
        if(post.getIsSelf() == false) {
            loadImage(feedViewHolder.contentImage, post.getPreview().getImageUrl());
            feedViewHolder.contentText.setVisibility(View.GONE);
        }else{
            String text = post.getSelfTextHTML();
            if(text != null) {
                feedViewHolder.contentText.setText(Html.fromHtml(text));
            }else{
                feedViewHolder.contentText.setVisibility(View.GONE);
                Log.i("archit",post.getTitle());
            }
            feedViewHolder.contentImage.setVisibility(View.GONE);
        }
        if(feedResponse.getImageURL() == null){
            Drawable  drawable = mContext.getResources().getDrawable(R.drawable.ic_perm_identity);
            feedViewHolder.sourceImage.setImageDrawable(drawable);

        }else{
            loadImageRounded(feedViewHolder.sourceImage,feedResponse.getImageURL());
        }
    }

    @Override
    public int getItemCount() {
        return mFeedResponseList.size();
    }

    private void loadImage(ImageView imageView,String url){
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_report_problem);
        Glide.with(mContext)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }
    private void loadImageRounded(ImageView imageView,String url){
       RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_perm_identity).circleCrop();
       Glide.with(mContext)
               .load(url)
               .apply(requestOptions).into(imageView);
    }
}
