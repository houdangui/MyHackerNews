package com.hackernews.dangui.myhackernews.model;

import java.util.ArrayList;

/**
 * Created by dangui on 1/3/17.
 */

public class Comment {
    private String by;
    private Long id;
    private Long[] kids;
    private Long parent;
    private String text;
    private Long time;
    private String type;
    private boolean deleted;
    private ItemFetchStatus status;
    private Comment latestReply;

    public Comment(Long id) {
        this.id = id;
        this.by = "";
        this.kids = new Long[0];
        this.time = 0L;
        this.parent = 0L;
        this.type = "";
        this.text = "";
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long[] getKids() {
        return kids;
    }

    public void setKids(Long[] kids) {
        this.kids = kids;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ItemFetchStatus getStatus() {
        return status;
    }

    public void setStatus(ItemFetchStatus status) {
        this.status = status;
    }

    public Comment getLatestReply() {
        return latestReply;
    }

    public void setLatestReply(Comment latestReply) {
        this.latestReply = latestReply;
    }

    public boolean shouldFetchLatestReply() {
        if (kids != null && kids.length >= 1) {
            if (latestReply == null) {
                return true;
            }
        }

        return false;
    }

    public Long getLastestReplyId() {
        Long id = null;
        if (kids != null && kids.length >= 1) {
            id = kids[0];
        }
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}


/*
{
  "by" : "norvig",
  "id" : 2921983,
  "kids" : [ 2922097, 2922429, 2924562, 2922709, 2922573, 2922140, 2922141 ],
  "parent" : 2921506,
  "text" : "Aw shucks, guys ... you make me blush with your compliments.<p>Tell you what, Ill make a deal: I'll keep writing if you keep reading. K?",
  "time" : 1314211127,
  "type" : "comment"
}
 */
