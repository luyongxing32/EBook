/**
 *
 * EBookContentProvider.java
 * com.twoservices.ebook.providers
 *
 * Created by Ry on 5/26/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook.providers;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import com.twoservices.ebook.providers.Chapters.Chapter;
import com.twoservices.ebook.providers.MacroAreas.MacroArea;
import com.twoservices.ebook.providers.Modules.Module;
import com.twoservices.ebook.providers.Topics.Topic;
import com.twoservices.ebook.utils.RestoreDatabase;

import java.util.HashMap;

/**
 *
 *
 */
public class EBookContentProvider extends ContentProvider {

    private static final String TAG = EBookContentProvider.class.getSimpleName();

    /**
     * File name of database.
     */
    public static final String DATABASE_NAME = "content.db";
    /**
     * Database version.
     */
    private static final int DATABASE_VERSION = 3;

    public static final String AUTHORITY = "com.twoservices.ebook.providers.EBookContentProvider";

    protected static final String AREAS_TABLE_NAME = "area";
    protected static final String CHAPTER_TABLE_NAME = "chapter";
    protected static final String TOPIC_TABLE_NAME = "topic";
    protected static final String MODULE_TABLE_NAME = "module";

    private static final int MACROAREAS = 1;
    private static final int CHAPTERS = 2;
    private static final int TOPICS = 3;
    private static final int MODULES = 4;

    private static HashMap<String, String> areasProjectionMap;
    private static HashMap<String, String> chaptersProjectionMap;
    private static HashMap<String, String> topicsProjectionMap;
    private static HashMap<String, String> modulesProjectionMap;

    private static final UriMatcher sUriMatcher;

    private DatabaseHelper dbHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, AREAS_TABLE_NAME, MACROAREAS);
        sUriMatcher.addURI(AUTHORITY, CHAPTER_TABLE_NAME, CHAPTERS);
        sUriMatcher.addURI(AUTHORITY, TOPIC_TABLE_NAME, TOPICS);
        sUriMatcher.addURI(AUTHORITY, MODULE_TABLE_NAME, MODULES);

        areasProjectionMap = new HashMap<String, String>();
        areasProjectionMap.put(MacroArea.AREA_ID, MacroArea.AREA_ID);
        areasProjectionMap.put(MacroArea.AREA_FGCOLOR, MacroArea.AREA_FGCOLOR);
        areasProjectionMap.put(MacroArea.AREA_BGCOLOR, MacroArea.AREA_BGCOLOR);
        areasProjectionMap.put(MacroArea.AREA_TITLE, MacroArea.AREA_TITLE);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//            db.execSQL("CREATE TABLE IF NOT EXISTS " + AREAS_TABLE_NAME + " ("
//                    + MacroArea.AREA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + MacroArea.AREA_FGCOLOR + " TEXT, "
//                    + MacroArea.AREA_BGCOLOR + " VARCHAR(255), "
//                    + MacroAreas.MacroArea.AREA_TITLE + " TEXT, "
//                    + "UNIQUE (" + MacroArea.AREA_BGCOLOR + ", " + MacroArea.AREA_TITLE + ") ON CONFLICT REPLACE)");

            Log.w(TAG, "Append field '_id' to " + AREAS_TABLE_NAME + " in database.");
            db.execSQL("ALTER TABLE " + AREAS_TABLE_NAME + " RENAME TO temp_table");
            db.execSQL("CREATE TABLE " + AREAS_TABLE_NAME + " ("
                    + MacroArea.AREA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MacroArea.AREA_FGCOLOR + " INTEGER, "
                    + MacroArea.AREA_BGCOLOR + " INTEGER, "
                    + MacroArea.AREA_TITLE + " TEXT)");
            db.execSQL("INSERT INTO " + AREAS_TABLE_NAME + " ("
                    + MacroArea.AREA_FGCOLOR + ", "
                    + MacroArea.AREA_BGCOLOR + ", "
                    + MacroArea.AREA_TITLE
                    + ") SELECT fgcolor,bgcolor,title FROM temp_table");
            db.execSQL("DROP TABLE temp_table");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
//                    + ", which will destroy all old data");
//            //db.execSQL("DROP TABLE IF EXISTS " + AREAS_TABLE_NAME);
//            db.execSQL("ALTER TABLE " + AREAS_TABLE_NAME + " RENAME TO temp_table");
//            db.execSQL("CREATE TABLE " + AREAS_TABLE_NAME + " ("
//                    + MacroArea.AREA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + MacroArea.AREA_FGCOLOR + " INTEGER, "
//                    + MacroArea.AREA_BGCOLOR + " INTEGER, "
//                    + MacroArea.AREA_TITLE + " TEXT");
//            db.execSQL("INSERT INTO " + AREAS_TABLE_NAME + " ("
//                    + MacroArea.AREA_ID + ", "
//                    + MacroArea.AREA_FGCOLOR + ", "
//                    + MacroArea.AREA_BGCOLOR + ", "
//                    + MacroArea.AREA_TITLE + ", "
//                    + ") SELECT sfgcolor,bgcolor,title FROM temp_table");
//            db.execSQL("DROP TABLE temp_table");
//            onCreate(db);
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case MACROAREAS:
                count = db.delete(AREAS_TABLE_NAME, selection, selectionArgs);
                break;

            case CHAPTERS:
                count = db.delete(CHAPTER_TABLE_NAME, selection, selectionArgs);
                break;

            case TOPICS:
                count = db.delete(TOPIC_TABLE_NAME, selection, selectionArgs);
                break;

            case MODULES:
                count = db.delete(MODULE_TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MACROAREAS:
                return MacroArea.CONTENT_TYPE;

            case CHAPTERS:
                return Chapter.CONTENT_TYPE;

            case TOPICS:
                return Topic.CONTENT_TYPE;

            case MODULES:
                return Module.CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        long rowId = -1;

        switch (sUriMatcher.match(uri)) {
            case MACROAREAS:
                rowId = db.insert(AREAS_TABLE_NAME, null, values);
                if (rowId > -1) {
                    Uri areaUri = ContentUris.withAppendedId(MacroArea.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(areaUri, null);
                    return areaUri;
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case CHAPTERS:
                rowId = db.insert(CHAPTER_TABLE_NAME, null, values);
                if (rowId > -1) {
                    Uri chapterUri = ContentUris.withAppendedId(Chapter.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(chapterUri, null);
                    return chapterUri;
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case TOPICS:
                rowId = db.insert(TOPIC_TABLE_NAME, null, values);
                if (rowId > -1) {
                    Uri topicUri = ContentUris.withAppendedId(Topic.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(topicUri, null);
                    return topicUri;
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case MODULES:
                rowId = db.insert(MODULE_TABLE_NAME, null, values);
                if (rowId > -1) {
                    Uri moduleUri = ContentUris.withAppendedId(Module.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(moduleUri, null);
                    return moduleUri;
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();

        // First, Copy database file from assets
        if (!RestoreDatabase.isExistsFile(context)) {
            RestoreDatabase.extractDB(context);
        }

        this.dbHelper = new DatabaseHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case MACROAREAS:
                qb.setTables(AREAS_TABLE_NAME);
                qb.setProjectionMap(areasProjectionMap);
                break;

            case CHAPTERS:
                qb.setTables(CHAPTER_TABLE_NAME);
                qb.setProjectionMap(chaptersProjectionMap);
                break;

            case TOPICS:
                qb.setTables(TOPIC_TABLE_NAME);
                qb.setProjectionMap(topicsProjectionMap);
                break;

            case MODULES:
                qb.setTables(MODULE_TABLE_NAME);
                qb.setProjectionMap(modulesProjectionMap);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case MACROAREAS:
                count = db.update(AREAS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case CHAPTERS:
                count = db.update(CHAPTER_TABLE_NAME, values, selection, selectionArgs);
                break;

            case TOPICS:
                count = db.update(TOPIC_TABLE_NAME, values, selection, selectionArgs);
                break;

            case MODULES:
                count = db.update(MODULE_TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
