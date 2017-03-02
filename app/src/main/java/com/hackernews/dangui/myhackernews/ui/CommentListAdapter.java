package com.hackernews.dangui.myhackernews.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackernews.dangui.myhackernews.R;
import com.hackernews.dangui.myhackernews.model.Comment;
import com.hackernews.dangui.myhackernews.model.Story;
import com.hackernews.dangui.myhackernews.util.CommentsListListener;
import com.hackernews.dangui.myhackernews.util.NewsListListener;

import java.util.ArrayList;

/**
 * Created by dangui on 2/3/17.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private ArrayList<Comment> mDataSet;
    private CommentsListListener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mRootView;
        public TextView mTvIndex;
        public TextView mTvPoints;
        public TextView mTvTitle;
        public TextView mTvSite;
        public TextView mTvTimestamp;
        public TextView mTvCommentNum;
        public ImageView mIvComment;
        public ImageButton mBtnOpenBrowser;

        public ViewHolder(View v) {
            super(v);
            mRootView = v;
            mTvIndex = (TextView) v.findViewById(R.id.index);
            mTvPoints = (TextView) v.findViewById(R.id.points);
            mTvTitle = (TextView) v.findViewById(R.id.title);
            mTvSite = (TextView) v.findViewById(R.id.site_url);
            mTvTimestamp = (TextView) v.findViewById(R.id.timestamp);
            mTvCommentNum = (TextView) v.findViewById(R.id.comment_num);
            mIvComment = (ImageView) v.findViewById(R.id.comment_icon);
            mBtnOpenBrowser = (ImageButton) v.findViewById(R.id.open_browser);
        }
    }

    public CommentListAdapter(ArrayList<Comment> comments, CommentsListListener listener) {
        mDataSet = comments;
        mListener = listener;
    }

    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CommentListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
