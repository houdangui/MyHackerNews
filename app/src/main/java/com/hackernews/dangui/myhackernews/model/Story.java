package com.hackernews.dangui.myhackernews.model;

import java.util.ArrayList;

/**
 * Created by dangui on 1/3/17.
 */

public class Story {
    private String by;
    private Integer descendants;
    private Integer id;
    private ArrayList<Integer> kids;
    private Integer score;
    private Integer time;
    private String title;
    private String type;
    private String url;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Integer> getKids() {
        return kids;
    }

    public void setKids(ArrayList<Integer> kids) {
        this.kids = kids;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
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
}


/*
{
  "by" : "dhouston",
  "descendants" : 71,
  "id" : 8863,
  "kids" : [ 8952, 9224, 8917, 8884, 8887, 8943, 8869, 8958, 9005, 9671, 8940, 9067, 8908, 9055, 8865, 8881, 8872, 8873, 8955, 10403, 8903, 8928, 9125, 8998, 8901, 8902, 8907, 8894, 8878, 8870, 8980, 8934, 8876 ],
  "score" : 111,
  "time" : 1175714200,
  "title" : "My YC app: Dropbox - Throw away your USB drive",
  "type" : "story",
  "url" : "http://www.getdropbox.com/u/2/screencast.html"
}
 */