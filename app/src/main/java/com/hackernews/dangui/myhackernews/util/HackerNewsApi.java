package com.hackernews.dangui.myhackernews.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by dangui on 28/2/17.
 */
public class HackerNewsApi {
    private static final String TAG = "HackerNewsApi";
    private static final String URL_TOP_STORIES = "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty";
    private static final String URL_ITEM = "https://hacker-news.firebaseio.com/v0/item/%d.json?print=pretty";

    private static HackerNewsApi ourInstance = new HackerNewsApi();

    public static HackerNewsApi getInstance() {
        return ourInstance;
    }

    private HackerNewsApi() {

    }

    public void fetchTopStories(Context context) {
        Log.d(TAG, "-> fetchTopStories");
        HttpRequestHelper.getInstance().get(context, URL_TOP_STORIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "<- fetchTopStories response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    }

    public void fetchStoryDetail() {

    }
}
