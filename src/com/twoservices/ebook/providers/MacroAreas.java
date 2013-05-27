/**
 *
 * MacroAreas.java
 * com.twoservices.ebook.providers
 *
 * Created by Ry on 5/26/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public class MacroAreas {

    public static final class MacroArea implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + EBookContentProvider.AUTHORITY + "/" + EBookContentProvider.AREAS_TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ebook.area";

        public static final String AREA_ID = "_id";
        public static final String AREA_FGCOLOR = "fgcolor";
        public static final String AREA_BGCOLOR = "bgcolor";
        public static final String AREA_TITLE = "title";
    }

}
