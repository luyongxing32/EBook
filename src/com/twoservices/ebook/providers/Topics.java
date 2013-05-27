/**
 *
 * Topics.java
 * com.twoservices.ebook.providers
 *
 * Created by Ry on 5/26/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public class Topics {

    public static final class Topic implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + EBookContentProvider.AUTHORITY + "/" + EBookContentProvider.TOPIC_TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ebook.topic";

        public static final String TOPIC_ID = "id";
        public static final String TOPIC_CHAPTER_ID = "chapter_id";
        public static final String TOPIC_TITLE = "title";
    }

}
