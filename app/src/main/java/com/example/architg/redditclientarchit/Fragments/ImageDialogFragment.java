package com.example.architg.redditclientarchit.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.architg.redditclientarchit.R;

/**
 * Created by archit.g on 31/08/17.
 */

public class ImageDialogFragment extends DialogFragment {
    public static ImageDialogFragment getInstance(String url){
        ImageDialogFragment imageDialogFragment = new ImageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        imageDialogFragment.setArguments(bundle);
        return imageDialogFragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
       // dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.full_screen_image);
        String url = getArguments().getString("url");
        final ImageView imageView = dialog.findViewById(R.id.image_view);
        final Context context = getContext();
        final ProgressBar progressBar = dialog.findViewById(R.id.progress);
        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_error));
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
        return dialog;
    }
    @Override
    public void onResume(){
        super.onResume();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        getDialog().getWindow().setLayout(width,height);
    }
}
