package com.hackernews.dangui.myhackernews.util;

import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by dangui on 1/3/17.
 */

public class Utils {

    public static void DebugLog(String tag, String message) {
        Log.e(tag, message);
    }

    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
