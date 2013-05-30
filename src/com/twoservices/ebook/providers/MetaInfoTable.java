/**
 *
 * MetaInfoTable.java
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
public class MetaInfoTable {

    private static final String TAG = MetaInfoTable.class.getSimpleName();
    private static final MetaInfoTable instance = new MetaInfoTable();

    /**
     * Private constructor
     */
    private MetaInfoTable() {
    }

    /**
     * Gets a instance of MetaInfoTable
     */
    public static MetaInfoTable getInstance() {

        return instance;
    }

    /**
     * Gets data of all metainfo
     *
     * @param contentResolver
     * @return
     */
    public Cursor getMetaInfoData(ContentResolver contentResolver, String selection, String[] selectionArgs) {
        return contentResolver.query(EBook.MetaInfo.CONTENT_URI, MetaInfoDataQuery.PROJECTION, selection, selectionArgs, null);
    }

    /**
     * Topics data query parameters. Projection columns from indices defined
     * according to projection.
     */
    public interface MetaInfoDataQuery {
        /* Here we define columns which we need */
        String[] PROJECTION = {
                EBook.MetaInfo.METAINFO_ID + " AS _id",
                EBook.MetaInfo.METAINFO_PARAM,
                EBook.MetaInfo.METAINFO_VALUE,
        };

        /* Here we define columns indices according to PROJECTION */
        int METAINFO_ID = 0;
        int METAINFO_PARAM = 1;
        int METAINFO_VALUE = 2;
    }
}
