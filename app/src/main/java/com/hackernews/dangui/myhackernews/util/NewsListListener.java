package com.hackernews.dangui.myhackernews.util;

import com.hackernews.dangui.myhackernews.model.Story;

/**
 * Created by dangui on 1/3/17.
 */

public interface NewsListListener {
    public void onEmptyStoryShown(Story story);
    public void onStoryClicked(Story story);
}
