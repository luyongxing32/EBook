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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.twoservices.ebook.providers.EBook.Chapter;
import com.twoservices.ebook.providers.EBook.MacroArea;
import com.twoservices.ebook.providers.EBook.Media;
import com.twoservices.ebook.providers.EBook.MetaInfo;
import com.twoservices.ebook.providers.EBook.Module;
import com.twoservices.ebook.providers.EBook.Template;
import com.twoservices.ebook.providers.EBook.Topic;
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
    protected static final String METAINFO_TABLE_NAME = "metainfo";
    protected static final String TEMPLATE_TABLE_NAME = "template";
    protected static final String MEDIA_TABLE_NAME = "media";

    private static final int MACROAREA = 1;
    private static final int CHAPTER = 2;
    private static final int TOPIC = 3;
    private static final int MODULE = 4;
    private static final int METAINFO = 5;
    private static final int TEMPLATE = 6;
    private static final int MEDIA = 7;

    private static HashMap<String, String> areasProjectionMap;
    private static HashMap<String, String> chaptersProjectionMap;
    private static HashMap<String, String> topicsProjectionMap;
    private static HashMap<String, String> modulesProjectionMap;
    private static HashMap<String, String> metainfosProjectionMap;
    private static HashMap<String, String> templatesProjectionMap;
    private static HashMap<String, String> mediasProjectionMap;

    private static final UriMatcher sUriMatcher;

    private DatabaseHelper dbHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, AREAS_TABLE_NAME, MACROAREA);
        sUriMatcher.addURI(AUTHORITY, CHAPTER_TABLE_NAME, CHAPTER);
        sUriMatcher.addURI(AUTHORITY, TOPIC_TABLE_NAME, TOPIC);
        sUriMatcher.addURI(AUTHORITY, MODULE_TABLE_NAME, MODULE);
        sUriMatcher.addURI(AUTHORITY, METAINFO_TABLE_NAME, METAINFO);
        sUriMatcher.addURI(AUTHORITY, TEMPLATE_TABLE_NAME, TEMPLATE);
        sUriMatcher.addURI(AUTHORITY, MEDIA_TABLE_NAME, MEDIA);

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
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case MACROAREA:
                count = db.delete(AREAS_TABLE_NAME, selection, selectionArgs);
                break;

            case CHAPTER:
                count = db.delete(CHAPTER_TABLE_NAME, selection, selectionArgs);
                break;

            case TOPIC:
                count = db.delete(TOPIC_TABLE_NAME, selection, selectionArgs);
                break;

            case MODULE:
                count = db.delete(MODULE_TABLE_NAME, selection, selectionArgs);
                break;

            case TEMPLATE:
                count = db.delete(TEMPLATE_TABLE_NAME, selection, selectionArgs);
                break;

            case MEDIA:
                count = db.delete(MEDIA_TABLE_NAME, selection, selectionArgs);
                break;

            case METAINFO:
                count = db.delete(METAINFO_TABLE_NAME, selection, selectionArgs);
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
            case MACROAREA:
                return MacroArea.CONTENT_TYPE;

            case CHAPTER:
                return Chapter.CONTENT_TYPE;

            case TOPIC:
                return Topic.CONTENT_TYPE;

            case MODULE:
                return Module.CONTENT_TYPE;

            case TEMPLATE:
                return Template.CONTENT_TYPE;

            case MEDIA:
                return Media.CONTENT_TYPE;

            case METAINFO:
                return MetaInfo.CONTENT_TYPE;

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
            case MACROAREA:
                rowId = db.insert(AREAS_TABLE_NAME, null, values);
                if (rowId > -1) {
                    Uri areaUri = ContentUris.withAppendedId(MacroArea.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(areaUri, null);
                    return areaUri;
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case CHAPTER:
                rowId = db.insert(CHAPTER_TABLE_NAME, null, values);
                if (rowId > -1) {
                    Uri chapterUri = ContentUris.withAppendedId(Chapter.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(chapterUri, null);
                    return chapterUri;
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case TOPIC:
                rowId = db.insert(TOPIC_TABLE_NAME, null, values);
                if (rowId > -1) {
                    Uri topicUri = ContentUris.withAppendedId(Topic.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(topicUri, null);
                    return topicUri;
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case MODULE:
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
            case MACROAREA:
                qb.setTables(AREAS_TABLE_NAME);
                qb.setProjectionMap(areasProjectionMap);
                break;

            case CHAPTER:
                qb.setTables(CHAPTER_TABLE_NAME);
                qb.setProjectionMap(chaptersProjectionMap);
                break;

            case TOPIC:
                qb.setTables(TOPIC_TABLE_NAME);
                qb.setProjectionMap(topicsProjectionMap);
                break;

            case MODULE:
                qb.setTables(MODULE_TABLE_NAME);
                qb.setProjectionMap(modulesProjectionMap);
                break;

            case TEMPLATE:
                qb.setTables(TEMPLATE_TABLE_NAME);
                qb.setProjectionMap(templatesProjectionMap);
                break;

            case MEDIA:
                qb.setTables(MEDIA_TABLE_NAME);
                qb.setProjectionMap(mediasProjectionMap);
                break;

            case METAINFO:
                qb.setTables(METAINFO_TABLE_NAME);
                qb.setProjectionMap(metainfosProjectionMap);
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
            case MACROAREA:
                count = db.update(AREAS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case CHAPTER:
                count = db.update(CHAPTER_TABLE_NAME, values, selection, selectionArgs);
                break;

            case TOPIC:
                count = db.update(TOPIC_TABLE_NAME, values, selection, selectionArgs);
                break;

            case MODULE:
                count = db.update(MODULE_TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
