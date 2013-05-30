/**
 *
 * EBook.java
 * com.twoservices.ebook.providers
 *
 * Created by Ry on 5/26/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public class EBook {

    public static final String DEFAULT_ID = "rowid";

    public static final class MetaInfo implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + EBookContentProvider.AUTHORITY + "/" + EBookContentProvider.METAINFO_TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ebook.metainfo";

        public static final String METAINFO_ID = DEFAULT_ID;
        public static final String METAINFO_PARAM = "param";
        public static final String METAINFO_VALUE = "val";
    }


    public static final class MacroArea implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + EBookContentProvider.AUTHORITY + "/" + EBookContentProvider.AREAS_TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ebook.area";

        public static final String AREA_ID = DEFAULT_ID;
        public static final String AREA_FGCOLOR = "fgcolor";
        public static final String AREA_BGCOLOR = "bgcolor";
        public static final String AREA_TITLE = "title";
    }


    public static final class Chapter implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + EBookContentProvider.AUTHORITY + "/" + EBookContentProvider.CHAPTER_TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ebook.chapter";

        public static final String CHAPTER_ID = DEFAULT_ID;
        public static final String CHAPTER_AREA_ID = "area_id";
        public static final String CHAPTER_TITLE = "title";
        public static final String CAHPTER_FLAGS = "flags";
    }


    public static final class Topic implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + EBookContentProvider.AUTHORITY + "/" + EBookContentProvider.TOPIC_TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ebook.topic";

        public static final String TOPIC_ID = DEFAULT_ID;
        public static final String TOPIC_CHAPTER_ID = "chapter_id";
        public static final String TOPIC_TITLE = "title";
    }


    public static final class Module implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + EBookContentProvider.AUTHORITY + "/" + EBookContentProvider.MODULE_TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ebook.module";

        public static final String MODULE_ID = DEFAULT_ID;
        public static final String MODULE_TOPIC_ID = "topic_id";
        public static final String MODULD_TITLE = "title";
        public static final String MODULE_THUMB_ID = "thumbId";
        public static final String MODULE_TEMPLATES = "templates";
    }


    public static final class Template implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + EBookContentProvider.AUTHORITY + "/" + EBookContentProvider.TEMPLATE_TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ebook.template";

        public static final String TEMPLATE_ID = DEFAULT_ID;
        public static final String TEMPLATE_MODULE_ID = "module_id";
        public static final String TEMPLATE_TEXT = "text";
        public static final String TEMPLATE_IMAGES = "images";
    }


    public static final class Media implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + EBookContentProvider.AUTHORITY + "/" + EBookContentProvider.MEDIA_TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ebook.media";

        public static final String MEDIA_ID = DEFAULT_ID;
        public static final String MEDIA_VALUE = "value";
    }

}
