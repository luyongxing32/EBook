package com.twoservices.ebook.providers;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import com.twoservices.ebook.providers.User.Users;

import java.util.HashMap;

/**
 *
 *
 */
public class UsersContentProvider extends ContentProvider {
    private static final String TAG = UsersContentProvider.class.getSimpleName();

    /**
     * File name of database.
     */
    private static final String DATABASE_NAME = "quantumlife.db";
    /**
     * Database version.
     */
    private static final int DATABASE_VERSION = 4;

    public static final String AUTHORITY = "com.rosberry.infinity.providers.UsersContentProvider";

    protected static final String USERS_TABLE_NAME = "users";
    protected static final String CATEGORIES_TABLE_NAME = "categories";
    protected static final String ITEMS_TABLE_NAME = "items";
    protected static final String FREQUENCIES_TABLE_NAME = "frequencies";

    private static final int USERS = 1;
    private static final int CATEGORIES = 2;
    private static final int ITEMS = 3;
    private static final int FREQUENCIES = 4;

    private static HashMap<String, String> usersProjectionMap;
    private static HashMap<String, String> categoriesProjectionMap;
    private static HashMap<String, String> itemsProjectionMap;
    private static HashMap<String, String> frequenciesProjectionMap;

    private static final UriMatcher sUriMatcher;

    private DatabaseHelper dbHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, USERS_TABLE_NAME, USERS);
        sUriMatcher.addURI(AUTHORITY, CATEGORIES_TABLE_NAME, CATEGORIES);
        sUriMatcher.addURI(AUTHORITY, ITEMS_TABLE_NAME, ITEMS);
        sUriMatcher.addURI(AUTHORITY, FREQUENCIES_TABLE_NAME, FREQUENCIES);

        usersProjectionMap = new HashMap<String, String>();
        usersProjectionMap.put(Users.USER_ID, User.Users.USER_ID);
        usersProjectionMap.put(Users.USER_IMAGE, Users.USER_IMAGE);
        usersProjectionMap.put(Users.FIRST_NAME, Users.FIRST_NAME);
        usersProjectionMap.put(Users.LAST_NAME, Users.LAST_NAME);
        usersProjectionMap.put(Users.BIRTH_DATE, Users.BIRTH_DATE);
        usersProjectionMap.put(Users.CHARACTERISTICS, Users.CHARACTERISTICS);
        usersProjectionMap.put(Users.CUSTOMIZE_SELECTED, Users.CUSTOMIZE_SELECTED);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + USERS_TABLE_NAME + " ("
                    + Users.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Users.USER_IMAGE + " TEXT, "
                    + Users.FIRST_NAME + " VARCHAR(255), "
                    + Users.LAST_NAME + " TEXT, "
                    + Users.BIRTH_DATE + " TEXT, "
                    + Users.CHARACTERISTICS + " LONGTEXT, "
                    + Users.CUSTOMIZE_SELECTED + " BOOLEAN, "
                    + "UNIQUE (" + Users.FIRST_NAME + ", " + Users.LAST_NAME + ") ON CONFLICT REPLACE)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            //db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
            db.execSQL("ALTER TABLE " + USERS_TABLE_NAME + " RENAME TO temp_table");
            db.execSQL("CREATE TABLE " + USERS_TABLE_NAME + " ("
                    + Users.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Users.USER_IMAGE + " TEXT, "
                    + Users.FIRST_NAME + " VARCHAR(255), "
                    + Users.LAST_NAME + " TEXT, "
                    + Users.BIRTH_DATE + " TEXT, "
                    + Users.CHARACTERISTICS + " LONGTEXT, "
                    + Users.CUSTOMIZE_SELECTED + " BOOLEAN, "
                    + "UNIQUE (" + Users.FIRST_NAME + ", " + Users.LAST_NAME + ") ON CONFLICT REPLACE)");
            db.execSQL("INSERT INTO " + USERS_TABLE_NAME + " ("
                    + Users.USER_ID + ", "
                    + Users.USER_IMAGE + ", "
                    + Users.FIRST_NAME + ", "
                    + Users.LAST_NAME + ", "
                    + Users.BIRTH_DATE + ", "
                    + Users.CHARACTERISTICS
                    + ") SELECT _id,image,fistsName,lastName,birthData,characteristics FROM temp_table");
            db.execSQL("DROP TABLE temp_table");
            onCreate(db);
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case USERS:
                count = db.delete(USERS_TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case USERS:
                return Users.CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        long rowId = -1;

        switch (sUriMatcher.match(uri)) {
            case USERS:
                rowId = db.insert(USERS_TABLE_NAME, null, values);
                if (rowId > -1) {
                    Uri userUri = ContentUris.withAppendedId(Users.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(userUri, null);
                    return userUri;
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        this.dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case USERS:
                qb.setTables(USERS_TABLE_NAME);
                qb.setProjectionMap(usersProjectionMap);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case USERS:
                count = db.update(USERS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case CATEGORIES:
                count = db.update(CATEGORIES_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ITEMS:
                count = db.update(ITEMS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case FREQUENCIES:
                count = db.update(FREQUENCIES_TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
