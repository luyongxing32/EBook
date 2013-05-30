/**
 *
 * TemplateTable.java
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
public class TemplateTable {

    private static final String TAG = TemplateTable.class.getSimpleName();
    private static final TemplateTable instance = new TemplateTable();

    /**
     * Private constructor
     */
    private TemplateTable() {
    }

    /**
     * Gets a instance of TemplateTable
     */
    public static TemplateTable getInstance() {

        return instance;
    }

    /**
     * Gets data of all templates
     *
     * @param contentResolver
     * @return
     */
    public Cursor getTemplateData(ContentResolver contentResolver, String selection, String[] selectionArgs) {
        return contentResolver.query(EBook.Template.CONTENT_URI, TemplateDataQuery.PROJECTION, selection, selectionArgs, null);
    }

    /**
     * Topics data query parameters. Projection columns from indices defined
     * according to projection.
     */
    public interface TemplateDataQuery {
        /* Here we define columns which we need */
        String[] PROJECTION = {
                EBook.Template.TEMPLATE_ID + " AS _id",
                EBook.Template.TEMPLATE_MODULE_ID,
                EBook.Template.TEMPLATE_TEXT,
                EBook.Template.TEMPLATE_IMAGES,
        };

        /* Here we define columns indices according to PROJECTION */
        int TEMPLATE_ID = 0;
        int TEMPLATE_MODULE_ID = 1;
        int TEMPLATE_TEXT = 2;
        int TEMPLATE_IMAGES = 3;
    }
}
