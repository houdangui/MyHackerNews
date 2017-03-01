package com.hackernews.dangui.myhackernews.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hackernews.dangui.myhackernews.R;

/**
 * Created by dangui on 1/3/17.
 */

public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
    }

}
