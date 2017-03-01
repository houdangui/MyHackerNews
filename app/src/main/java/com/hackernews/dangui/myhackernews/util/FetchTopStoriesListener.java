package com.hackernews.dangui.myhackernews.util;

/**
 * Created by dangui on 1/3/17.
 */

public interface FetchTopStoriesListener {
    public void onActionSuccess(Long[] ids);
    public void onActionFail(String errorMessage);
}
