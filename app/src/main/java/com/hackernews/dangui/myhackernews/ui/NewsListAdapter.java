package com.hackernews.dangui.myhackernews.ui;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackernews.dangui.myhackernews.R;
import com.hackernews.dangui.myhackernews.model.Story;
import com.hackernews.dangui.myhackernews.util.HackerNewsApi;
import com.hackernews.dangui.myhackernews.util.NewsListListener;
import com.hackernews.dangui.myhackernews.util.TimeAgo;
import com.hackernews.dangui.myhackernews.util.Utils;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by dangui on 1/3/17.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    private ArrayList<Story> mDataSet;
    private NewsListListener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mRootView;
        public TextView mTvIndex;
        public TextView mTvPoints;
        public TextView mTvTitle;
        public TextView mTvSite;
        public TextView mTvTimestamp;
        public TextView mTvCommentNum;
        public ImageView mIvComment;
        
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
        }
    }

    public NewsListAdapter(ArrayList<Story> stories, NewsListListener listener) {
        mDataSet = stories;
        mListener = listener;
    }

    @Override
    public NewsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Story story = mDataSet.get(position);
        if (TextUtils.isEmpty(story.getUrl())) {
            mListener.onEmptyStoryShown(story);
            holder.mTvIndex.setText(String.valueOf(position));
            holder.mTvPoints.setText("...");
            holder.mTvTitle.setText("...");
            holder.mTvSite.setText("...");
            holder.mTvTimestamp.setText("...");
            holder.mTvCommentNum.setText("...");
            holder.mIvComment.setVisibility(View.INVISIBLE);

        } else {
            holder.mTvIndex.setText(String.valueOf(position));
            holder.mTvPoints.setText("+ " + story.getScore());
            holder.mTvTitle.setText(story.getTitle());
            holder.mTvSite.setText(getDisplayUrl(story.getUrl()));
            holder.mTvTimestamp.setText(TimeAgo.toDuration(System.currentTimeMillis() - story.getTime()) +
                    " - " + story.getBy());
            holder.mTvCommentNum.setText(String.valueOf(story.getDescendants()));
            holder.mIvComment.setVisibility(View.VISIBLE);
        }
        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStoryClicked(story);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private String getDisplayUrl(String url) {
        String displayUrl = url;
        try {
            String domain = Utils.getDomainName(url);
            displayUrl = domain;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return displayUrl;
    }
}
