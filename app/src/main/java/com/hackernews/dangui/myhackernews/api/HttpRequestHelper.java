package com.hackernews.dangui.myhackernews.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by dangui on 28/2/17.
 */

public class HttpRequestHelper {
    private static HttpRequestHelper ourInstance = new HttpRequestHelper();

    public static HttpRequestHelper getInstance() {
        return ourInstance;
    }

    private RequestQueue requestQueue = null;

    private HttpRequestHelper() {

    }

    public RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public void get(Context context, String url, Response.Listener<String> listener,
                    Response.ErrorListener errorListener) {
        sendHttpRequest(context, Request.Method.GET, url, listener, errorListener);
    }

    public void post(Context context, String url, Response.Listener<String> listener,
                     Response.ErrorListener errorListener) {
        sendHttpRequest(context, Request.Method.POST, url, listener, errorListener);
    }

    public void sendHttpRequest(Context context, int method, String url, Response.Listener<String> listener,
                                Response.ErrorListener errorListener) {
        RequestQueue queue = getRequestQueue(context);

        StringRequest stringRequest = new StringRequest(method, url, listener,
                errorListener){

        };

        queue.add(stringRequest);
    }
}
