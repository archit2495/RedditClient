package com.example.architg.redditclientarchit.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.architg.redditclientarchit.Activity.CommentsActivity;
import com.example.architg.redditclientarchit.Model.Root;
import com.example.architg.redditclientarchit.Network.Loader;
import com.example.architg.redditclientarchit.R;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archit.g on 08/09/17.
 */

public class CommentFragment extends Fragment {
    String mType;
    LinearLayout mRootView;
    List<String> colors = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initColors();
        mType = getArguments().getString("type");
    }
    private void initColors(){
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
        View v = inflater.inflate(R.layout.demo, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        mRootView = view.findViewById(R.id.root_view);
        loadData();
    }

    void loadData() {
        Futures.addCallback((ListenableFuture<Root>) ((CommentsActivity)getActivity()).getLoader().loadComments(mType),
                new FutureCallback<Root>() {
                    @Override
                    public void onSuccess(Root info) {
                        commentInfoToView(info.commentInfos.get(1),mRootView,0,0);
                    }
                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }
    public void commentInfoToView(Root.CommentInfo commentInfo,LinearLayout layout,int padding,int colorIndex){
        for(int i = 0;i < commentInfo.getInfoData().getComments().size();i++){
            Root.CommentInfo.InfoData.Comment comment = commentInfo.getInfoData().getComments().get(i);
            if(comment.getKind().equals("more")){
                TextView textView = (TextView)LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.load_more,null,false);
                layout.addView(textView);
            }else{
                layout.addView(getSingleItemView(comment,padding,colorIndex));
                if(comment.getData().getReplies() != null) {
                    commentInfoToView(comment.getData().getReplies(),layout,padding + 5,colorIndex + 1);
                    //tmp.addView(commentInfoToView(comment.getData().getReplies()));
                }
            }
        }
    }
    public LinearLayout getSingleItemView(Root.CommentInfo.InfoData.Comment comment,int padding,int colorIndex){
        LinearLayout linearLayout = (LinearLayout)LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.comment_single_item,null,false);
        View line = linearLayout.findViewById(R.id.line);
        line.setBackgroundColor(Color.parseColor(colors.get(colorIndex%10)));
        LinearLayout commentLayout = linearLayout.findViewById(R.id.comment);
        commentLayout.setPadding(padding,0,0,0);
        TextView author = linearLayout.findViewById(R.id.author);
        author.setText("Posted by " + comment.getData().author);
        TextView score = linearLayout.findViewById(R.id.score);
        score.setText(comment.getData().ups + " points");
        TextView time = linearLayout.findViewById(R.id.time);
        time.setText(DateUtils.getRelativeTimeSpanString(comment.getData().created_utc * 1000l).toString());
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
