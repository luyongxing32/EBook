/**
 *
 * TopicTable.java
 * com.twoservices.ebook.providers
 *
 *
 * Created by Ry on 5/26/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 */

package com.twoservices.ebook.providers;

import android.content.ContentResolver;
import android.database.Cursor;

/**
 * Database operation wrapper class
 */
public class TopicTable {

    private static final String TAG = TopicTable.class.getSimpleName();
    private static final TopicTable instance = new TopicTable();

    /**
     * Private constructor
     */
    private TopicTable() {
    }

    /**
     * Gets a instance of TopicTable
     */
    public static TopicTable getInstance() {

        return instance;
    }

    /**
     * Gets data of all topics
     *
     * @param contentResolver
     * @return
     */
    public Cursor getTopicData(ContentResolver contentResolver, String selection, String[] selectionArgs) {
        return contentResolver.query(EBook.Topic.CONTENT_URI, TopicDataQuery.PROJECTION, selection, selectionArgs, null);
    }

    /**
     * Topics data query parameters. Projection columns from indices defined
     * according to projection.
     */
    public interface TopicDataQuery {
        /* Here we define columns which we need */
        String[] PROJECTION = {
                EBook.Topic.TOPIC_ID + " AS _id",
                EBook.Topic.TOPIC_CHAPTER_ID,
                EBook.Topic.TOPIC_TITLE,
        };

        /* Here we define columns indices according to PROJECTION */
        int TOPIC_ID = 0;
        int TOPIC_CHAPTER_ID = 1;
        int TOPIC_TITLE = 2;
    }
}
