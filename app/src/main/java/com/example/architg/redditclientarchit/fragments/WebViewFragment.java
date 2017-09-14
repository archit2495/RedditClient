package com.example.architg.redditclientarchit.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.architg.redditclientarchit.R;

/**
 * Created by archit.g on 31/08/17.
 */

public class WebViewFragment extends BottomSheetDialogFragment {
    public static WebViewFragment getInstance(String url){
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        webViewFragment.setArguments(bundle);
        return webViewFragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.web_view);
        String url = getArguments().getString("url");
        WebView webView = dialog.findViewById(R.id.web_view_client);
        webView.loadUrl(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
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
