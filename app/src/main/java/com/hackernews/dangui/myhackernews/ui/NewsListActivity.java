package com.hackernews.dangui.myhackernews.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hackernews.dangui.myhackernews.R;
import com.hackernews.dangui.myhackernews.model.Story;
import com.hackernews.dangui.myhackernews.util.HackerNewsApi;

import java.util.ArrayList;

public class NewsListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<Story> mTopStories;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mNewsList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mNewsList = (RecyclerView) findViewById(R.id.news_list);
        mLayoutManager = new LinearLayoutManager(this);
        mNewsList.setLayoutManager(mLayoutManager);
        mTopStories = new ArrayList<>();
        mAdapter = new NewsListAdapter(mTopStories);
        mNewsList.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {

    }

    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //HackerNewsApi.getInstance().fetchTopStories(this);
}
