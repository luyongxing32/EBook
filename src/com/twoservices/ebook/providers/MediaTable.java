/**
 *
 * MediaTable.java
 * com.twoservices.ebook.providers
 *
 *
 * Created by Ry on 5/29/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 */

package com.twoservices.ebook.providers;

import android.content.ContentResolver;
import android.database.Cursor;

/**
 * Database operation wrapper class
 */
public class MediaTable {

    private static final String TAG = MediaTable.class.getSimpleName();
    private static final MediaTable instance = new MediaTable();

    /**
     * Private constructor
     */
    private MediaTable() {
    }

    /**
     * Gets a instance of MetaInfoTable
     */
    public static MediaTable getInstance() {

        return instance;
    }

    /**
     * Gets data of all media
     *
     * @param contentResolver
     * @return
     */
    public Cursor getMediaData(ContentResolver contentResolver, String selection, String[] selectionArgs) {
        return contentResolver.query(EBook.Media.CONTENT_URI, MetaInfoDataQuery.PROJECTION, selection, selectionArgs, null);
    }

    /**
     * Topics data query parameters. Projection columns from indices defined
     * according to projection.
     */
    public interface MetaInfoDataQuery {
        /* Here we define columns which we need */
        String[] PROJECTION = {
                EBook.Media.MEDIA_ID + " AS _id",
                EBook.Media.MEDIA_VALUE,
        };

        /* Here we define columns indices according to PROJECTION */
        int MEDIA_ID = 0;
        int MEDIA_VALUE = 1;
    }
}
