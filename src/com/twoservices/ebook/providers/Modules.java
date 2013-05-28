/**
 *
 * Modules.java
 * com.twoservices.ebook.providers
 *
 * Created by Ry on 5/26/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public class Modules {

    public static final class Module implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + EBookContentProvider.AUTHORITY + "/" + EBookContentProvider.MODULE_TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ebook.module";

        public static final String MODULE_ID = "rowid";
        public static final String MODULE_TOPIC_ID = "topic_id";
        public static final String MODULD_TITLE = "title";
        public static final String MODULE_THUMB_ID = "thumbId";
        public static final String MODULE_TEMPLATES = "templates";
    }
}
