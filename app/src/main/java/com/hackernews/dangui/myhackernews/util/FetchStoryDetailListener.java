package com.hackernews.dangui.myhackernews.util;

import com.hackernews.dangui.myhackernews.model.Story;

/**
 * Created by dangui on 1/3/17.
 */

public interface FetchStoryDetailListener {
    void onActionSuccess(Story story);
    void onActionFail(String errorMessage);
}
