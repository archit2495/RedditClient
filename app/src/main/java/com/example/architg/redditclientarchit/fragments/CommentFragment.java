package com.example.architg.redditclientarchit.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.architg.redditclientarchit.R;
import com.example.architg.redditclientarchit.activity.CommentsActivity;
import com.example.architg.redditclientarchit.model.Root;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by archit.g on 08/09/17.
 */

public class CommentFragment extends Fragment {
    String mType;
    LinearLayout mRootView;
    List<String> colors = new ArrayList<String>();
    DotProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initColors();
        mType = getArguments().getString("type");
    }

    private void initColors() {
        colors.add("#0000ff");
        colors.add("#ff4040");
        colors.add("#98f5ff");
        colors.add("#8a2be2");
        colors.add("#ffd39b");
        colors.add("#7fff00");
        colors.add("#ff7f24");
        colors.add("#ffb90f");
        colors.add("#ff1493");
        colors.add("#ee6a50");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.comment_list_view, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRootView = view.findViewById(R.id.root_view);
        mProgressBar = view.findViewById(R.id.dot_progress_bar);
        loadData();
    }

    void loadData() {
        Futures.addCallback((ListenableFuture<Root>) ((CommentsActivity) getActivity()).getLoader().loadComments(mType),
                new FutureCallback<Root>() {
                    @Override
                    public void onSuccess(Root info) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
                        commentInfoToView(info.commentInfos.get(1), mRootView, 0, 0);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                }, Executors.newSingleThreadExecutor());
    }

    void loadHiddenCommentsData(String url, final View textView, final int padding, final int colorIndex) {

        Futures.addCallback((ListenableFuture<Root>) ((CommentsActivity) getActivity()).getLoader().loadHiddenComments(url), new FutureCallback<Root>() {

            @Override
            public void onSuccess(Root result) {
                LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(layoutParams);
                commentInfoToView(result.commentInfos.get(1), linearLayout,padding, colorIndex);
                ViewGroup parent = (ViewGroup) textView.getParent();
                int index = parent.indexOfChild(textView);
                parent.removeView(textView);
                parent.addView(linearLayout, index);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void commentInfoToView(Root.CommentInfo commentInfo, final LinearLayout layout, final int padding, final int colorIndex) {
        for (int i = 0; i < commentInfo.getInfoData().getComments().size(); i++) {
            final Root.CommentInfo.InfoData.Comment comment = commentInfo.getInfoData().getComments().get(i);
            if (comment.getKind() != null && comment.getKind().equals("more")){
                for (int j = 0; j < comment.getData().children.size(); j++) {
                    final TextView textView = (TextView) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.load_more, layout, false);
                    final int position = j;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loadHiddenCommentsData(comment.getData().children.get(position), textView,padding,colorIndex);
                        }
                    });

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            layout.addView(textView);
                        }
                    });
                }
            }else{
                final LinearLayout linearLayout = getSingleItemView(comment,padding,colorIndex);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        layout.addView(linearLayout);
                    }
                });
                if (comment.getData() != null && comment.getData().getReplies() != null) {
                    commentInfoToView(comment.getData().getReplies(), linearLayout, padding + 5, colorIndex + 1);
                }
            }
        }
    }

    public LinearLayout getSingleItemView(Root.CommentInfo.InfoData.Comment comment, int padding, int colorIndex) {
        if(comment.getData() == null){
            return null;
        }
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.comment_single_item, null, false);
        View line = linearLayout.findViewById(R.id.line);
        line.setBackgroundColor(Color.parseColor(colors.get(colorIndex % 10)));
        LinearLayout commentLayout = linearLayout.findViewById(R.id.comment);
        commentLayout.setPadding(padding, 0, 0, 0);
        TextView author = linearLayout.findViewById(R.id.author);
        author.setText(comment.getData().author);
        TextView score = linearLayout.findViewById(R.id.score);
        score.setText("\u2022"+ comment.getData().ups + " points");
        TextView time = linearLayout.findViewById(R.id.time);
        time.setText("\u2022" +DateUtils.getRelativeTimeSpanString(comment.getData().created_utc * 1000l).toString());
        TextView commentText = linearLayout.findViewById(R.id.comment_text);
        String text = comment.getData().body_html;
        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("&gt;", ">");
        commentText.setText(Html.fromHtml(text));
        return linearLayout;
    }

    public static CommentFragment getInstance(String type) {
        CommentFragment commentFragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        commentFragment.setArguments(bundle);
        return commentFragment;
    }
}
