/**
 *
 * Chapters.java
 * com.twoservices.ebook.providers
 *
 * Created by Ry on 5/26/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public class Chapters {

    public static final class Chapter implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + EBookContentProvider.AUTHORITY + "/" + EBookContentProvider.CHAPTER_TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ebook.chapter";

        public static final String CHAPTER_ID = "rowid";
        public static final String CHAPTER_AREA_ID = "area_id";
        public static final String CHAPTER_TITLE = "title";
        public static final String CAHPTER_FLAGS = "flags";
    }

}