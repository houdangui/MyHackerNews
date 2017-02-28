package com.hackernews.dangui.myhackernews.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hackernews.dangui.myhackernews.R;
import com.hackernews.dangui.myhackernews.util.HackerNewsApi;

public class NewsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        HackerNewsApi.getInstance().fetchTopStories(this);
    }
}
