package com.example.architg.redditclientarchit.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.example.architg.redditclientarchit.Model.RedditDisplayPost;
import com.example.architg.redditclientarchit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archit.g on 11/08/17.
 */

public class RedditPostListAdapter extends RecyclerView.Adapter<RedditPostListAdapter.FeedViewHolder> {
    private List<RedditDisplayPost> mRedditDisplayPostsList;
    private Context mContext;
    private int startIndex = 0;

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
        }
    }

    public RedditPostListAdapter(Context context) {
        mRedditDisplayPostsList = new ArrayList<>();
        mContext = context;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_single_item, parent, false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder feedViewHolder, int position) {
        RedditDisplayPost redditDisplayPost = mRedditDisplayPostsList.get(position);
        initPostHolder(feedViewHolder);
        feedViewHolder.heading.setText(redditDisplayPost.getHeading());
        feedViewHolder.time.setText(redditDisplayPost.getTime());
        feedViewHolder.name.setText(redditDisplayPost.getName());
        if (redditDisplayPost.getSourceImage() == null) {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_perm_identity);
            feedViewHolder.sourceImage.setImageDrawable(drawable);
        } else {
            loadImageRounded(feedViewHolder.sourceImage, redditDisplayPost.getSourceImage());
        }
        if (redditDisplayPost.getContentImageThumbnail() != null && !redditDisplayPost.getContentImageThumbnail().equals("self") && !redditDisplayPost.getContentImageThumbnail().equals("default")) {
            feedViewHolder.contentImageFrame.setVisibility(View.VISIBLE);
            loadImage(feedViewHolder.contentImage, feedViewHolder.contentImageProgress, redditDisplayPost.getContentImageThumbnail());
        }
        if (redditDisplayPost.getSelfHelpText() != null) {
            feedViewHolder.contentText.setVisibility(View.VISIBLE);
           String text = redditDisplayPost.getSelfHelpText();
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
        return mRedditDisplayPostsList.size();
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
                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_report_problem));
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

    public int update(List<RedditDisplayPost> redditDisplayPosts) {
        int start = mRedditDisplayPostsList.size();
        for (int i = 0; i < redditDisplayPosts.size(); i++) {                       //ask about adding in front of the list
            mRedditDisplayPostsList.add(redditDisplayPosts.get(i));
        }
        notifyItemRangeChanged(start, redditDisplayPosts.size());
        return start;
    }

    public void updateSourceImage(String imageUrl, int index) {
        mRedditDisplayPostsList.get(index).setSourceImage(imageUrl);
        notifyItemChanged(index);
    }
    public void flush(){
        mRedditDisplayPostsList.clear();
    }
    public int getListSize() {
        return mRedditDisplayPostsList.size();
    }
}
