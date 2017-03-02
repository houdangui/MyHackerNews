package com.hackernews.dangui.myhackernews.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hackernews.dangui.myhackernews.util.Constants;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dangui on 2/3/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public final static String TAG = "DataBaseHelper";
    public static final String DB_NAME = "hackernews_db";
    public static final int DB_VERSION = 1;
    private static DatabaseHelper sInstance;
    private AtomicInteger mOpenCounter = new AtomicInteger();
    private SQLiteDatabase mDatabase;

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();
        }
    }

    String createStoryTableSql = "create table if not exists " + Constants.STORY_TB + " ("+
            "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            Constants.StoryTableColumn.by + " varchar, "
            + Constants.StoryTableColumn.descendants + " integer, "
            + Constants.StoryTableColumn.id + " integer, "
            + Constants.StoryTableColumn.kids + " varchar, "
            + Constants.StoryTableColumn.score + " integer, "
            + Constants.StoryTableColumn.time + " integer, "
            + Constants.StoryTableColumn.title + " varchar, "
            + Constants.StoryTableColumn.type + " varchar, "
            + Constants.StoryTableColumn.url + " varchar, "
            + Constants.StoryTableColumn.text + " varchar, "
            + Constants.StoryTableColumn.fetched + " integer" +
            "); ";

    @Override
    public synchronized void onCreate(SQLiteDatabase db) {
        db.execSQL(createStoryTableSql);
    }

    @Override
    public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.STORY_TB);
        onCreate(db);
    }
}
