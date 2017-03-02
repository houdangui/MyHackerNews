package com.hackernews.dangui.myhackernews.util;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

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

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static String longArrayToString(Long[] array) {
        String res = Arrays.toString(array);
        return res;
    }

    public static Long[] stringToLongArray(String arr) {
        String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

        Long[] results = new Long[items.length];

        for (int i = 0; i < items.length; i++) {
            try {
                results[i] = Long.parseLong(items[i]);
            } catch (NumberFormatException nfe) {
                //NOTE: write something here if you need to recover from formatting errors
            };
        }
        return results;
    }

}
