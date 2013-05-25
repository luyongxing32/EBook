package com.twoservices.ebook.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public class User {

    public static final class Users implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://"
                + UsersContentProvider.AUTHORITY + "/" + UsersContentProvider.USERS_TABLE_NAME);

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.quantum.users";

        public static final String USER_ID = "_id";
        public static final String USER_IMAGE = "image";
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String BIRTH_DATE = "birthDate";
        public static final String CHARACTERISTICS = "characteristics";
        public static final String CUSTOMIZE_SELECTED = "customize_selected";
    }

}
