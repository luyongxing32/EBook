/**
 *
 * ChapterTable.java
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
public class ChapterTable {

    private static final String TAG = ChapterTable.class.getSimpleName();
    private static final ChapterTable instance = new ChapterTable();

    /**
     * Private constructor
     */
    private ChapterTable() {
    }

    /**
     * Gets a instance of ChapterTable
     */
    public static ChapterTable getInstance() {

        return instance;
    }

    /**
     * Gets data of all chapters
     *
     * @param contentResolver
     * @return
     */
    public Cursor getChapterData(ContentResolver contentResolver, String selection, String[] selectionArgs) {
        return contentResolver.query(Chapters.Chapter.CONTENT_URI, ChapterDataQuery.PROJECTION, selection, selectionArgs, null);
    }

    /**
     * Chapters data query parameters. Projection columns from indices defined
     * according to projection.
     */
    public interface ChapterDataQuery {
        /* Here we define columns which we need */
        String[] PROJECTION = {
                Chapters.Chapter.CHAPTER_AREA_ID,
                Chapters.Chapter.CHAPTER_ID,
                Chapters.Chapter.CHAPTER_TITLE,
                Chapters.Chapter.CAHPTER_FLAGS
        };

        /* Here we define columns indices according to PROJECTION */
        int CHAPTER_ID = 0;
        int CHAPTER_AREA_ID = 1;
        int CHAPTER_TITLE = 2;
        int CHAPTER_FLAGS = 3;
    }
}
