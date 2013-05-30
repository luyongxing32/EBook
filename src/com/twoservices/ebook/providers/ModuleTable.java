/**
 *
 * ModuleTable.java
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
public class ModuleTable {

    private static final String TAG = ModuleTable.class.getSimpleName();
    private static final ModuleTable instance = new ModuleTable();

    /**
     * Private constructor
     */
    private ModuleTable() {
    }

    /**
     * Gets a instance of ModuleTable
     */
    public static ModuleTable getInstance() {

        return instance;
    }

    /**
     * Gets data of all modules
     *
     * @param contentResolver
     * @return
     */
    public Cursor getModuleData(ContentResolver contentResolver, String selection, String[] selectionArgs) {
        return contentResolver.query(EBook.Module.CONTENT_URI, ModuleDataQuery.PROJECTION, selection, selectionArgs, null);
    }

    /**
     * Topics data query parameters. Projection columns from indices defined
     * according to projection.
     */
    public interface ModuleDataQuery {
        /* Here we define columns which we need */
        String[] PROJECTION = {
                EBook.Module.MODULE_ID + " AS _id",
                EBook.Module.MODULE_TOPIC_ID,
                EBook.Module.MODULD_TITLE,
                EBook.Module.MODULE_THUMB_ID,
                EBook.Module.MODULE_TEMPLATES
        };

        /* Here we define columns indices according to PROJECTION */
        int MODULE_ID = 0;
        int MODULE_TOPIC_ID = 1;
        int MODULE_TITLE = 2;
        int MODULE_THUMB_ID = 3;
        int MODULE_TEMPLATES = 4;
    }
}
