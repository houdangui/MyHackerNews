package com.hackernews.dangui.myhackernews.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.hackernews.dangui.myhackernews.model.ItemFetchStatus;
import com.hackernews.dangui.myhackernews.model.Story;
import com.hackernews.dangui.myhackernews.util.Constants;
import com.hackernews.dangui.myhackernews.util.Utils;

import java.util.ArrayList;

/**
 * Created by dangui on 2/3/17.
 */
public class StoryDBHelper {
    private static StoryDBHelper ourInstance = new StoryDBHelper();

    public static StoryDBHelper getInstance() {
        return ourInstance;
    }

    private StoryDBHelper() {
    }

    public void insertStory(Context context, Story story) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.StoryTableColumn.by, story.getBy());
        values.put(Constants.StoryTableColumn.descendants, story.getDescendants());
        values.put(Constants.StoryTableColumn.id, story.getId());
        values.put(Constants.StoryTableColumn.kids, Utils.longArrayToString(story.getKids()));
        values.put(Constants.StoryTableColumn.score, story.getScore());
        values.put(Constants.StoryTableColumn.time, story.getTime());
        values.put(Constants.StoryTableColumn.title, story.getTitle());
        values.put(Constants.StoryTableColumn.type, story.getType());
        values.put(Constants.StoryTableColumn.url, story.getUrl());
        values.put(Constants.StoryTableColumn.text, story.getText());
        values.put(Constants.StoryTableColumn.fetched, story.getStatus() == ItemFetchStatus.FETCHED_SUCCESS ? 1 : 0);

        long id = -1;
        try {
            id = db.insert(Constants.STORY_TB, null, values);
        } catch(SQLiteException e){
            e.printStackTrace();
        } finally {
            if (db != null){
                DatabaseHelper.getInstance(context).closeDatabase();
            }
        }
    }

    public ArrayList<Story> loadStories(Context context) {
        ArrayList<Story> stories = new ArrayList<>();
        SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase();

        Cursor cursor = db.query(Constants.STORY_TB, null, null, null, null, null, null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Story story = readStoryByCursor(db, cursor);
                stories.add(story);
                cursor.moveToNext();
            }
        } catch (SQLiteException exception) {
            exception.printStackTrace();
        } finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
            if (db != null){
                DatabaseHelper.getInstance(context).closeDatabase();
            }
        }

        return stories;
    }

    private Story readStoryByCursor(SQLiteDatabase db, Cursor cursor) {
        String by = cursor.getString(cursor.getColumnIndexOrThrow(Constants.StoryTableColumn.by));
        Integer descendants = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.StoryTableColumn.descendants));
        Long id = cursor.getLong(cursor.getColumnIndexOrThrow(Constants.StoryTableColumn.id));
        String kids = cursor.getString(cursor.getColumnIndexOrThrow(Constants.StoryTableColumn.kids));
        Long[] kidsArray = Utils.stringToLongArray(kids);
        Integer score = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.StoryTableColumn.score));
        Long time = cursor.getLong(cursor.getColumnIndexOrThrow(Constants.StoryTableColumn.time));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(Constants.StoryTableColumn.title));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(Constants.StoryTableColumn.type));
        String url = cursor.getString(cursor.getColumnIndexOrThrow(Constants.StoryTableColumn.url));
        String text = cursor.getString(cursor.getColumnIndexOrThrow(Constants.StoryTableColumn.text));
        Integer fetched = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.StoryTableColumn.fetched));

        Story story = new Story(id);
        story.setBy(by);
        story.setDescendants(descendants);
        story.setKids(kidsArray);
        story.setScore(score);
        story.setTime(time);
        story.setTitle(title);
        story.setType(type);
        story.setUrl(url);
        story.setText(text);
        story.setStatus(fetched == 1 ? ItemFetchStatus.FETCHED_SUCCESS : ItemFetchStatus.NEVER_FETCH);

        return story;
    }

    public int updateStory(Context context, Story story) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.StoryTableColumn.by, story.getBy());
        values.put(Constants.StoryTableColumn.descendants, story.getDescendants());
        values.put(Constants.StoryTableColumn.kids, Utils.longArrayToString(story.getKids()));
        values.put(Constants.StoryTableColumn.score, story.getScore());
        values.put(Constants.StoryTableColumn.time, story.getTime());
        values.put(Constants.StoryTableColumn.title, story.getTitle());
        values.put(Constants.StoryTableColumn.type, story.getType());
        values.put(Constants.StoryTableColumn.url, story.getUrl());
        values.put(Constants.StoryTableColumn.text, story.getText());
        values.put(Constants.StoryTableColumn.fetched, story.getStatus() == ItemFetchStatus.FETCHED_SUCCESS ? 1 : 0);

        int id = -1;
        try {
            id = db.update(Constants.STORY_TB, values, Constants.StoryTableColumn.id + "=?",
                    new String[]{"" + story.getId()});
        } catch (SQLiteException e){
            e.printStackTrace();
        } finally {
            if (db != null){
                DatabaseHelper.getInstance(context).closeDatabase();
            }
        }
        return id;
    }

    public void clearCache(Context context){
        SQLiteDatabase db = DatabaseHelper.getInstance(context).openDatabase();
        try {
            db.delete(Constants.STORY_TB, null, null);
        } catch (SQLiteException e){
            e.printStackTrace();
        } finally {
            if (db != null){
                DatabaseHelper.getInstance(context).closeDatabase();
            }
        }
    }
}
