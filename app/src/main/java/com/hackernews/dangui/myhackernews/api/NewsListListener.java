package com.hackernews.dangui.myhackernews.api;

import com.hackernews.dangui.myhackernews.model.Story;

/**
 * Created by dangui on 1/3/17.
 */

public interface NewsListListener {
    void onEmptyStoryShown(Story story);
    void onStoryClicked(Story story);
    void onOpenBrowser(Story story);
}
