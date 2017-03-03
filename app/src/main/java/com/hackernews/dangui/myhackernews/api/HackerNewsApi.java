package com.hackernews.dangui.myhackernews.api;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hackernews.dangui.myhackernews.model.Comment;
import com.hackernews.dangui.myhackernews.model.ItemFetchStatus;
import com.hackernews.dangui.myhackernews.model.Story;
import com.hackernews.dangui.myhackernews.util.Utils;

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
        String url = String.format(URL_ITEM, story.getId());
        Utils.DebugLog(TAG, "-> fetchStoryDetail:" + url);
        story.setStatus(ItemFetchStatus.FETCHING);
        HttpRequestHelper.getInstance().get(context, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.DebugLog(TAG, "<- fetchStoryDetail response: " + response);
                //parse the response
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String type = jsonObject.getString("type");
                    if (type.equals("story") || type.equals("job") || type.equals("poll")) {
                        String by = jsonObject.optString("by", "");
                        Integer descendants = jsonObject.optInt("descendants", 0);
                        Long id = jsonObject.getLong("id");

                        Long[] kids = new Long[0];
                        if (jsonObject.has("kids")) {
                            JSONArray kidsArray = jsonObject.getJSONArray("kids");
                            kids = new Long[kidsArray.length()];
                            for (int i = 0; i < kidsArray.length(); i++) {
                                Long kid = kidsArray.getLong(i);
                                kids[i] = kid;
                            }
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

                        story.setStatus(ItemFetchStatus.FETCHED_SUCCESS);
                        listener.onActionSuccess(story);
                    } else if (type.equals("comment") || type.equals("pollopt")) {
                        if (jsonObject.has("parent")) {
                            Long parent = jsonObject.getLong("parent");
                            story.setId(parent);
                            story.setUrl("");
                            //for triggering fetch the parent
                            story.setStatus(ItemFetchStatus.NEVER_FETCH);
                            listener.onActionSuccess(story);
                        }
                    }
                } catch (JSONException e){
                    story.setStatus(ItemFetchStatus.FETCHED_FAIL);
                    listener.onActionFail(e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                story.setStatus(ItemFetchStatus.FETCHED_FAIL);
                listener.onActionFail(error.getLocalizedMessage());
            }
        });
    }

    public void fetchCommentDetail(Context context, final Comment comment, final FetchCommentDetailListener listener) {
        String url = String.format(URL_ITEM, comment.getId());
        Utils.DebugLog(TAG, "-> fetchCommentDetail:" + url);
        comment.setStatus(ItemFetchStatus.FETCHING);
        HttpRequestHelper.getInstance().get(context, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.DebugLog(TAG, "<- fetchCommentDetail response: " + response);
                //parse the response
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String type = jsonObject.getString("type");
                    if (type.equals("comment")) {
                        String by = jsonObject.optString("by", "");
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
                        Long time = jsonObject.optLong("time", 0);
                        String text = jsonObject.optString("text", "");
                        Long parent = jsonObject.getLong("parent");
                        boolean deleted = jsonObject.optBoolean("deleted", false);

                        comment.setBy(by);
                        comment.setId(id);
                        comment.setKids(kids);
                        comment.setTime(time);
                        comment.setType(type);
                        comment.setText(text);
                        comment.setParent(parent);
                        comment.setDeleted(deleted);
                        comment.setStatus(ItemFetchStatus.FETCHED_SUCCESS);
                        listener.onActionSuccess(comment);
                    } else {
                        comment.setStatus(ItemFetchStatus.FETCHED_FAIL);
                        listener.onActionFail("Not an comment");
                    }
                } catch (JSONException e){
                    comment.setStatus(ItemFetchStatus.FETCHED_FAIL);
                    listener.onActionFail(e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                comment.setStatus(ItemFetchStatus.FETCHED_FAIL);
                listener.onActionFail(error.getLocalizedMessage());
            }
        });
    }
}
