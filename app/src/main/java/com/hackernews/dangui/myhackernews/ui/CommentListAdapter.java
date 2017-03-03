package com.hackernews.dangui.myhackernews.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackernews.dangui.myhackernews.R;
import com.hackernews.dangui.myhackernews.model.Comment;
import com.hackernews.dangui.myhackernews.model.ItemFetchStatus;
import com.hackernews.dangui.myhackernews.model.Story;
import com.hackernews.dangui.myhackernews.api.CommentsListListener;
import com.hackernews.dangui.myhackernews.util.TimeAgo;
import com.hackernews.dangui.myhackernews.util.Utils;

import java.util.ArrayList;

/**
 * Created by dangui on 2/3/17.
 */

public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context mContext;
    private Story mStory;
    private ArrayList<Comment> mDataSet;
    private CommentsListListener mListener;

    public static class VHItem extends RecyclerView.ViewHolder {
        public View mRootView;
        public TextView mTvTimestamp;
        public TextView mTvCommentContent;
        public TextView mTvReplyTimestamp;
        public TextView mTvReplyContent;
        public CardView mCvComment;
        public CardView mCvReply;

        public VHItem(View v) {
            super(v);
            mRootView = v;
            mTvTimestamp = (TextView) v.findViewById(R.id.timestamp);
            mTvCommentContent = (TextView) v.findViewById(R.id.content);
            mTvReplyTimestamp = (TextView) v.findViewById(R.id.reply_timestamp);
            mTvReplyContent = (TextView) v.findViewById(R.id.reply_content);
            mCvComment = (CardView) v.findViewById(R.id.cv_comment);
            mCvReply = (CardView) v.findViewById(R.id.cv_reply);
        }
    }

    public static class VHHeader extends RecyclerView.ViewHolder {
        public View mRootView;
        public TextView mTvTitle;
        public TextView mTvSite;
        public TextView mTvTimestamp;
        public TextView mTvCommentsNum;

        public VHHeader(View v) {
            super(v);
            mRootView = v;
            mTvTitle = (TextView) v.findViewById(R.id.title);
            mTvSite = (TextView) v.findViewById(R.id.site_url);
            mTvTimestamp = (TextView) v.findViewById(R.id.timestamp);
            mTvCommentsNum = (TextView) v.findViewById(R.id.comment_num);
        }
    }

    public CommentListAdapter(Context context, ArrayList<Comment> comments, Story story, CommentsListListener listener) {
        mContext = context;
        mDataSet = comments;
        mListener = listener;
        mStory = story;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM ) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comments, parent, false);
            VHItem vh = new VHItem(v);
            return vh;
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comments_header, parent, false);
            CommentListAdapter.VHHeader vh = new CommentListAdapter.VHHeader(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof  VHItem) {
            VHItem holder = (VHItem) viewHolder;
            Comment comment = mDataSet.get(position - 1);

            if (comment.getBy().equals("hermitdev")) {
                Utils.DebugLog("", "");
            }

            if (comment.getStatus() == ItemFetchStatus.FETCHED_SUCCESS) {
                setTimestampByText(holder.mTvTimestamp, comment);

                if (comment.isDeleted()) {
                    holder.mTvCommentContent.setText(mContext.getString(R.string.comment_deleted));
                } else {
                    holder.mTvCommentContent.setText(Utils.fromHtml(comment.getText()));
                }
            } else {
                mListener.onEmptyCommentShown(comment);
                holder.mTvTimestamp.setText("...");
                holder.mTvCommentContent.setText("...");
            }
            //latest reply of this comment
            Comment reply = comment.getLatestReply();
            if (reply == null) {
                holder.mCvReply.setVisibility(View.GONE);
            } else {
                if (reply.getStatus() == ItemFetchStatus.FETCHED_SUCCESS) {
                    setTimestampByText(holder.mTvReplyTimestamp, reply);
                    if (reply.isDeleted()) {
                        holder.mTvReplyContent.setText(mContext.getString(R.string.comment_deleted));
                    } else {
                        holder.mTvReplyContent.setText(Utils.fromHtml(reply.getText()));
                    }
                } else {
                    holder.mTvReplyTimestamp.setText("...");
                    holder.mTvReplyContent.setText("...");
                }
                holder.mCvReply.setVisibility(View.VISIBLE);
            }
        } else if (viewHolder instanceof VHHeader) {
            VHHeader holder = (VHHeader) viewHolder;
            holder.mTvTitle.setText(mStory.getTitle());
            holder.mTvSite.setText(mStory.getUrl());
            if (mStory.getTime() == null) {
                holder.mTvTimestamp.setText(mStory.getBy());
            } else {
                holder.mTvTimestamp.setText(formatDateString(TimeAgo.toDuration(System.currentTimeMillis() - mStory.getTime() * 1000), mStory.getBy()));
            }
            int kidsCount = mStory.getDescendants();
            String commentTitle = mContext.getResources().getQuantityString(R.plurals.comment_count, kidsCount, kidsCount);
            holder.mTvCommentsNum.setText(commentTitle);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size() + 1;
    }

    private Spannable formatDateString(String time, String by) {
        String str = time + " - " + by;
        Spannable sb = new SpannableString(str);
        sb.setSpan(new ForegroundColorSpan(0xffff6600), str.indexOf(by), str.indexOf(by) + by.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    private void setTimestampByText(TextView textView, Comment comment) {
        Long time = comment.getTime();
        String by = comment.getBy();
        if (time == null) {
            if (TextUtils.isEmpty(by)) {
                textView.setText("");
            } else {
                textView.setText(by);
            }
        } else {
            if (TextUtils.isEmpty(by)) {
                //for deleted comment
                textView.setText(TimeAgo.toDuration(System.currentTimeMillis() - time * 1000));
            } else {
                textView.setText(formatDateString(TimeAgo.toDuration(System.currentTimeMillis() - time * 1000), by));
            }
        }

    }
}
