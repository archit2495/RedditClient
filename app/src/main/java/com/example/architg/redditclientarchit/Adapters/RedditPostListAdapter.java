package com.example.architg.redditclientarchit.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.architg.redditclientarchit.Model.Info;
import com.example.architg.redditclientarchit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archit.g on 11/08/17.
 */

public class RedditPostListAdapter extends RecyclerView.Adapter<RedditPostListAdapter.FeedViewHolder> {
    private List<Info.Data.FeedResponse> mFeedResponses;
    private Context mContext;
    private FragmentListener fragmentListener;

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        public TextView name, time, heading, contentText;
        public ImageView sourceImage, contentImage;
        public FrameLayout contentImageFrame;
        public ProgressBar contentImageProgress;

        public FeedViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            time = view.findViewById(R.id.time);
            heading = view.findViewById(R.id.heading);
            sourceImage = view.findViewById(R.id.sourceImage);
            contentImage = view.findViewById(R.id.contentImage);
            contentText = view.findViewById(R.id.contentText);
            contentImageFrame = view.findViewById(R.id.contentImageFrame);
            contentImageProgress = view.findViewById(R.id.progress);
            contentImageFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentListener.showImageFragment(mFeedResponses.get(getAdapterPosition()).getPost().getPreview().getImageUrl());
                }
            });
            heading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentListener.showWebFragment(mFeedResponses.get(getAdapterPosition()).getPost().getUrl());
                }
            });
        }
    }

    public RedditPostListAdapter(Context context,FragmentListener fragmentListener) {
        mFeedResponses = new ArrayList<>();
        this.fragmentListener = fragmentListener;
        mContext = context;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_listview_single_item, parent, false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder feedViewHolder, int position) {
        Info.Data.FeedResponse feedResponse = mFeedResponses.get(position);
        initPostHolder(feedViewHolder);
        feedViewHolder.heading.setText(feedResponse.getPost().getTitle());
        feedViewHolder.time.setText(DateUtils.getRelativeTimeSpanString(feedResponse.getPost().getCreated()*1000l).toString());
        feedViewHolder.name.setText(feedResponse.getPost().getSubreddit_name_prefixed());
        if (feedResponse.getImageURL() == null) {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_perm_identity);
            feedViewHolder.sourceImage.setImageDrawable(drawable);
        } else {
            loadImageRounded(feedViewHolder.sourceImage, feedResponse.getImageURL());
        }
        if (feedResponse.getPost()!= null && feedResponse.getPost().getPreview() != null && feedResponse.getPost().getPreview().getImageUrl() != null) {
            feedViewHolder.contentImageFrame.setVisibility(View.VISIBLE);
            loadImage(feedViewHolder.contentImage, feedViewHolder.contentImageProgress, feedResponse.getPost().getPreview().getImageUrl());
        }
        if (feedResponse.getPost().getSelfTextHTML() != null) {
            feedViewHolder.contentText.setVisibility(View.VISIBLE);
           String text = feedResponse.getPost().getSelfTextHTML();
           text = text.replaceAll("&lt;","<");
           text= text.replaceAll("&gt;",">");
            feedViewHolder.contentText.setText(Html.fromHtml(text));
        }
    }
    private void initPostHolder(FeedViewHolder feedViewHolder){
        feedViewHolder.contentText.setVisibility(View.GONE);
        feedViewHolder.contentImageFrame.setVisibility(View.GONE);
    }
    @Override
    public int getItemCount() {
        return mFeedResponses.size();
    }

    private void loadImage(final ImageView imageView, final ProgressBar progressBar, final String url) {
        Log.i("url",url);
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(url)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_error));
                        progressBar.setVisibility(View.GONE);

                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    private void loadImageRounded(ImageView imageView, String url) {
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_perm_identity).circleCrop();
        Glide.with(mContext)
                .load(url)
                .apply(requestOptions).into(imageView);
    }

    public int update(List<Info.Data.FeedResponse> feedResponses) {
        int start = mFeedResponses.size();
        for (int i = 0; i < feedResponses.size(); i++) {                       //ask about adding in front of the list
            mFeedResponses.add(feedResponses.get(i));
        }
        notifyItemRangeChanged(start, feedResponses.size());
        return start;
    }

    public void updateSourceImage(String imageUrl, int index) {
        mFeedResponses.get(index).setImageURL(imageUrl);
        notifyItemChanged(index);
    }
    public void flush(){
        if(mFeedResponses != null)
        mFeedResponses.clear();
    }
    public int getListSize() {
        return mFeedResponses.size();
    }
    public Info.Data.FeedResponse getItem(int position){return  mFeedResponses.get(position);}

    public interface FragmentListener{
        void showImageFragment(String url);
        void showWebFragment(String url);
    }
}

