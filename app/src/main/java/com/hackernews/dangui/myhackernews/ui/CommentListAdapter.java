package com.hackernews.dangui.myhackernews.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackernews.dangui.myhackernews.R;
import com.hackernews.dangui.myhackernews.model.Comment;
import com.hackernews.dangui.myhackernews.model.Story;
import com.hackernews.dangui.myhackernews.util.CommentsListListener;
import com.hackernews.dangui.myhackernews.util.TimeAgo;

import java.util.ArrayList;

/**
 * Created by dangui on 2/3/17.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private ArrayList<Comment> mDataSet;
    private CommentsListListener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mRootView;
        public TextView mTvTimestamp;
        public TextView mTvCommentContent;

        public ViewHolder(View v) {
            super(v);
            mRootView = v;
            mTvTimestamp = (TextView) v.findViewById(R.id.timestamp);
            mTvCommentContent = (TextView) v.findViewById(R.id.content);
        }
    }

    public CommentListAdapter(ArrayList<Comment> comments, CommentsListListener listener) {
        mDataSet = comments;
        mListener = listener;
    }

    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comments, parent, false);
        CommentListAdapter.ViewHolder vh = new CommentListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CommentListAdapter.ViewHolder holder, int position) {
        Comment comment = mDataSet.get(position);
        if (comment.getTime() == null) {
            holder.mTvTimestamp.setText(comment.getBy());
        } else {
            holder.mTvTimestamp.setText(TimeAgo.toDuration(System.currentTimeMillis() - comment.getTime() * 1000) +
                    " - " + comment.getBy());
        }
        holder.mTvCommentContent.setText(comment.getText());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
