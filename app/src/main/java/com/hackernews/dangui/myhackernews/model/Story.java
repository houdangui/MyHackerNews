package com.hackernews.dangui.myhackernews.model;

import java.util.ArrayList;

/**
 * Created by dangui on 1/3/17.
 */

public class Story {
    private String by;
    private Integer descendants;
    private Long id;
    private Long[] kids;
    private Integer score;
    private Long time;
    private String title;
    private String type;
    private String url;
    private String text;
    private ItemFetchStatus status;

    public Story(Long id) {
        this.id = id;
        this.by = "";
        this.descendants = 0;
        this.kids = new Long[0];
        this.score = 0;
        this.time = 0L;
        this.title = "";
        this.type = "";
        this.url = "";
        this.text = "";
        status = ItemFetchStatus.NEVER_FETCHED;
    }

    public Story(String by, Integer descendants, Long id, Long[] kids, Integer score, Long time,
                 String title, String type, String url, String text) {
        this.by = by;
        this.descendants = descendants;
        this.id = id;
        this.kids = kids;
        this.score = score;
        this.time = time;
        this.title = title;
        this.type = type;
        this.url = url;
        this.text = text;
        status = ItemFetchStatus.NEVER_FETCHED;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public Integer getDescendants() {
        return descendants;
    }

    public void setDescendants(Integer descendants) {
        this.descendants = descendants;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ItemFetchStatus getStatus() {
        return status;
    }

    public void setStatus(ItemFetchStatus status) {
        this.status = status;
    }

}

