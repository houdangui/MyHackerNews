package com.hackernews.dangui.myhackernews.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.hackernews.dangui.myhackernews.R;
import com.hackernews.dangui.myhackernews.database.DatabaseHelper;
import com.hackernews.dangui.myhackernews.database.StoryDBHelper;
import com.hackernews.dangui.myhackernews.model.ItemFetchStatus;
import com.hackernews.dangui.myhackernews.model.Story;
import com.hackernews.dangui.myhackernews.api.FetchStoryDetailListener;
import com.hackernews.dangui.myhackernews.api.FetchTopStoriesListener;
import com.hackernews.dangui.myhackernews.api.HackerNewsApi;
import com.hackernews.dangui.myhackernews.api.NewsListListener;

import java.util.ArrayList;

public class NewsListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NewsListListener {
    private ArrayList<Story> mTopStories;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mNewsList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static int CACHE_LIMIT = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mNewsList = (RecyclerView) findViewById(R.id.news_list);
        mLayoutManager = new LinearLayoutManager(this);
        mNewsList.setLayoutManager(mLayoutManager);
        mTopStories = StoryDBHelper.getInstance().loadStories(this);
        mAdapter = new NewsListAdapter(mTopStories, this);
        mNewsList.setAdapter(mAdapter);

        mSwipeRefreshLayout.setRefreshing(true);
        fetchTopStories();

        getSupportActionBar().setTitle(getString(R.string.top_stories));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        fetchTopStories();
    }

    private void fetchTopStories() {
        HackerNewsApi.getInstance().fetchTopStories(this, new FetchTopStoriesListener() {
            @Override
            public void onActionSuccess(Long[] ids) {
                mTopStories.clear();
                StoryDBHelper.getInstance().clearCache(NewsListActivity.this);

                for (int i = 0; i < ids.length; i++) {
                    Long id = ids[i];
                    Story story = new Story(id);
                    story.setStatus(ItemFetchStatus.NEVER_FETCHED);
                    mTopStories.add(story);
                    if (i < CACHE_LIMIT) {
                        StoryDBHelper.getInstance().insertStory(NewsListActivity.this, story);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                mSwipeRefreshLayout.setRefreshing(false);


            }

            @Override
            public void onActionFail(String errorMessage) {
                Toast.makeText(NewsListActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onEmptyStoryShown(Story story) {
        if (story.getStatus() == ItemFetchStatus.NEVER_FETCHED) {
            HackerNewsApi.getInstance().fetchStoryDetail(this, story, new FetchStoryDetailListener() {
                @Override
                public void onActionSuccess(Story story) {
                    StoryDBHelper.getInstance().updateStory(NewsListActivity.this, story);
                    refreshListDelay();
                }

                @Override
                public void onActionFail(String errorMessage) {
                    Toast.makeText(NewsListActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onStoryClicked(Story story) {
        if (story.getStatus() == ItemFetchStatus.FETCHED) {
            Intent intent = new Intent(this, CommentsActivity.class);
            intent.putExtra("storyId", story.getId().longValue());
            intent.putExtra("url", story.getUrl() == null ? "" : story.getUrl());
            intent.putExtra("title", story.getTitle());
            intent.putExtra("timestamp", story.getTime().longValue());
            intent.putExtra("by", story.getBy());
            intent.putExtra("kids", story.getKids());
            intent.putExtra("descendants", story.getDescendants().intValue());
            intent.putExtra("type", story.getType());
            startActivity(intent);
        }
    }

    @Override
    public void onOpenBrowser(Story story) {
        String url = story.getUrl();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                TextUtils.isEmpty(url) ? Uri.parse("about:blank") : Uri.parse(url));
        startActivity(browserIntent);
    }

    private boolean isToRefresh = false;

    private void refreshListDelay() {
        if (isToRefresh) {
            return;
        }
        isToRefresh = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                isToRefresh = false;
            }
        }, 300);
    }
}
