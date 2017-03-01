package com.hackernews.dangui.myhackernews.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hackernews.dangui.myhackernews.model.ItemFetchStatus;
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
        story.setStatus(ItemFetchStatus.FETCHING);
        HttpRequestHelper.getInstance().get(context, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.DebugLog(TAG, "<- fetchStoryDetail response: " + response);
                boolean isToFetchParent = false;
                //parse the response
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String type = jsonObject.getString("type");
                    if (type.equals("story") || type.equals("job") || type.equals("poll")) {
                        String by = jsonObject.optString("by", "");
                        Integer descendants = jsonObject.optInt("descendants", 0);
                        Long id = jsonObject.getLong("id");

                        Long[] kids;
                        if (jsonObject.has("kids")) {
                            JSONArray kidsArray = jsonObject.getJSONArray("kids");
                            kids = new Long[kidsArray.length()];
                            for (int i = 0; i < kidsArray.length(); i++) {
                                Long kid = kidsArray.getLong(i);
                                kids[i] = kid;
                            }
                        } else {
                            kids = new Long[0];
                        }
                        Integer score = jsonObject.optInt("score", 0);
                        Long time = jsonObject.optLong("time", 0);
                        String title = jsonObject.optString("title", "");
                        String url = jsonObject.optString("url", "");
                        String text = jsonObject.optString("text", "");

                        story.setBy(by);
                        story.setDescendants(descendants);
                        story.setId(id);
                        story.setKids(kids);
                        story.setScore(score);
                        story.setTime(time);
                        story.setTitle(title);
                        story.setType(type);
                        story.setUrl(url);
                        story.setText(text);

                        listener.onActionSuccess(story);
                    } else if (type.equals("comment") || type.equals("pollopt")) {
                        if (jsonObject.has("parent")) {
                            Long parent = jsonObject.getLong("parent");
                            story.setId(parent);
                            story.setUrl("");
                            //for triggering fetch the parent
                            story.setStatus(ItemFetchStatus.NEVER_FETCHED);
                            isToFetchParent = true;
                            listener.onActionSuccess(story);
                        }
                    }
                } catch (JSONException e){
                    listener.onActionFail(e.getLocalizedMessage());
                } finally {
                    if (!isToFetchParent) {
                        story.setStatus(ItemFetchStatus.FETCHED);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onActionFail(error.getLocalizedMessage());
                story.setStatus(ItemFetchStatus.FETCHED);
            }
        });
    }
}
