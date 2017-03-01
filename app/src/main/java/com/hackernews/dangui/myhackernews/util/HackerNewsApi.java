package com.hackernews.dangui.myhackernews.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hackernews.dangui.myhackernews.model.Story;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public void fetchTopStories(final Context context, final FetchTopStoriesListener listener) {
        Utils.DebugLog(TAG, "-> fetchTopStories");
        HttpRequestHelper.getInstance().get(context, URL_TOP_STORIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.DebugLog(TAG, "<- fetchTopStories response: " + response);
                //parse the response
                //example: [ 9127232, 9128437, 9130049, 9130144, 9130064, 9130028, 9129409, 9127243, 9128571, ..., 9120990 ]
                try {
                    JSONArray array = new JSONArray(response);
                    Long[] ids = new Long[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        Long id = array.getLong(i);
                        ids[i] = id;
                    }
                    listener.onActionSuccess(ids);
                } catch (JSONException e){
                    listener.onActionFail(e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onActionFail(error.getLocalizedMessage());
            }
        });
    }

    public void fetchStoryDetail(Context context, final Story story, final FetchStoryDetailListener listener) {
        Utils.DebugLog(TAG, "-> fetchStoryDetail");
        String url = String.format(URL_ITEM, story.getId());
        HttpRequestHelper.getInstance().get(context, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.DebugLog(TAG, "<- fetchStoryDetail response: " + response);
                //parse the response
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String by = jsonObject.getString("by");
                    Integer descendants = jsonObject.getInt("descendants");
                    Long id = jsonObject.getLong("id");
                    JSONArray kidsArray = jsonObject.getJSONArray("kids");
                    Long[] kids = new Long[kidsArray.length()];
                    for (int i = 0; i < kidsArray.length(); i++) {
                        Long kid = kidsArray.getLong(i);
                        kids[i] = kid;
                    }
                    Integer score = jsonObject.getInt("score");
                    Long time = jsonObject.getLong("time");
                    String title = jsonObject.getString("title");
                    String type = jsonObject.getString("type");
                    String url = jsonObject.getString("url");

                    story.setBy(by);
                    story.setDescendants(descendants);
                    story.setId(id);
                    story.setKids(kids);
                    story.setScore(score);
                    story.setTime(time);
                    story.setTitle(title);
                    story.setType(type);
                    story.setUrl(url);

                    listener.onActionSuccess(story);
                } catch (JSONException e){
                    listener.onActionFail(e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onActionFail(error.getLocalizedMessage());
            }
        });
    }
}
