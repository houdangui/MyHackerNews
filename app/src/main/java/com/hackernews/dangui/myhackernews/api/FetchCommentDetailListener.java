package com.hackernews.dangui.myhackernews.api;

import com.hackernews.dangui.myhackernews.model.Comment;

/**
 * Created by dangui on 2/3/17.
 */

public interface FetchCommentDetailListener {
    void onActionSuccess(Comment comment);
    void onActionFail(String errorMessage);
}
