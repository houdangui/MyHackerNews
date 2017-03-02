package com.hackernews.dangui.myhackernews.ui;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackernews.dangui.myhackernews.R;
import com.hackernews.dangui.myhackernews.model.Comment;
import com.hackernews.dangui.myhackernews.model.ItemFetchStatus;
import com.hackernews.dangui.myhackernews.model.Story;
import com.hackernews.dangui.myhackernews.util.CommentsListListener;
import com.hackernews.dangui.myhackernews.util.TimeAgo;
import com.hackernews.dangui.myhackernews.util.Utils;

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
        public TextView mTvReplyTimestamp;
        public TextView mTvReplyContent;

        public ViewHolder(View v) {
            super(v);
            mRootView = v;
            mTvTimestamp = (TextView) v.findViewById(R.id.timestamp);
            mTvCommentContent = (TextView) v.findViewById(R.id.content);
            mTvReplyTimestamp = (TextView) v.findViewById(R.id.reply_timestamp);
            mTvReplyContent = (TextView) v.findViewById(R.id.reply_content);
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
        if (comment.getStatus() == ItemFetchStatus.FETCHED) {
            if (comment.getTime() == null) {
                holder.mTvTimestamp.setText(comment.getBy());
            } else {
                holder.mTvTimestamp.setText(TimeAgo.toDuration(System.currentTimeMillis() - comment.getTime() * 1000) +
                        " - " + comment.getBy());
            }
            holder.mTvCommentContent.setText(Utils.fromHtml(comment.getText()));
        } else {
            mListener.onEmptyCommentShown(comment);
            holder.mTvTimestamp.setText("...");
            holder.mTvCommentContent.setText("...");
        }
        //latest reply of this comment
        Comment reply = comment.getLatestReply();
        if (reply == null) {
            holder.mTvReplyTimestamp.setVisibility(View.GONE);
            holder.mTvReplyContent.setVisibility(View.GONE);
        } else {
            if (reply.getStatus() == ItemFetchStatus.FETCHED) {
                if (reply.getTime() == null) {
                    holder.mTvReplyTimestamp.setText(reply.getBy());
                } else {
                    holder.mTvReplyTimestamp.setText(TimeAgo.toDuration(System.currentTimeMillis() - reply.getTime() * 1000) +
                            " - " + reply.getBy());
                }
                holder.mTvReplyContent.setText(Utils.fromHtml(reply.getText()));
            } else {
                holder.mTvReplyTimestamp.setText("...");
                holder.mTvReplyContent.setText("...");
            }
            holder.mTvReplyTimestamp.setVisibility(View.VISIBLE);
            holder.mTvReplyContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
