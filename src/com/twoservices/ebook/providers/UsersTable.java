package com.twoservices.ebook.providers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.twoservices.ebook.Config;
import com.twoservices.ebook.providers.User.Users;

import java.util.List;

/**
 * Database operation wrapper class
 */
public class UsersTable {

    private static final String TAG = UsersTable.class.getSimpleName();
    private static final UsersTable instance = new UsersTable();

    /**
     * Private constructor
     */
    private UsersTable() {
    }

    /**
     * Gets a instance of UsersTable
     */
    public static UsersTable getInstance() {

        return instance;
    }

    /**
     * Adds a new user with a info
     *
     * @param contentResolver
     * @param firstName
     * @param lastName
     * @param date
     * @param charact
     */
    public long addNewUser(ContentResolver contentResolver, String userImagePath, String firstName,
                           String lastName, String date, String charact) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(Users.USER_IMAGE, userImagePath);
        contentValue.put(Users.FIRST_NAME, firstName);
        contentValue.put(Users.LAST_NAME, lastName);
        contentValue.put(Users.BIRTH_DATE, date);
        contentValue.put(Users.CHARACTERISTICS, charact);
        contentValue.put(Users.CUSTOMIZE_SELECTED, false);
        Uri uriAddResult = contentResolver.insert(Users.CONTENT_URI, contentValue);

        List<String> segments = uriAddResult.getPathSegments();

        long userid = Integer.parseInt(segments.get(segments.size() - 1));

        if (Config.DEBUG) Log.d(TAG, "new user has been added successfully - " + userid);

        return userid;
    }

    /**
     * Deletes a user with id
     *
     * @param contentResolver
     * @param userId
     */
    public void deleteUserRecord(ContentResolver contentResolver, String userId) {
        final String selection = Users.USER_ID + "=?";
        final String[] selectionArgs = {userId};
        contentResolver.delete(Users.CONTENT_URI, selection, selectionArgs);
    }

    /**
     * Checks to see if a note with a given title is in our database
     *
     * @param contentResolver
     * @param firstName
     * @return
     */
    public boolean isNoteInDB(ContentResolver contentResolver, String firstName) {
        boolean ret = false;
        String selectionArgs[] = {firstName};
        Cursor cursor = contentResolver.query(Users.CONTENT_URI, null, Users.FIRST_NAME + "=?", selectionArgs, null);
        if (null != cursor && cursor.moveToNext()) {
            ret = true;
        }

        if (null != cursor) {
            cursor.close();
        }
        return ret;
    }

    /**
     * Gets data of all users
     *
     * @param contentResolver
     * @return
     */
    public Cursor getUsersData(ContentResolver contentResolver, String selection, String[] selectionArgs) {
        //String[] projection = new String[] { Users.USER_ID,
        //                                     Users.USER_IMAGE,
        //                                     Users.FIRST_NAME,
        //                                     Users.LAST_NAME,
        //                                     Users.BIRTH_DATE,
        //                                     Users.CHARACTERISTICS
        //									   Users.CUSTOMIZE_SELECTED};
        return contentResolver.query(Users.CONTENT_URI, UserDataQuery.PROJECTION, selection, selectionArgs, Users.FIRST_NAME + " COLLATE NOCASE ASC");
    }

    /**
     * Updates a data with new Characteristics info
     *
     * @param contentResolver
     * @param userId
     * @param userImagePath
     * @param firstName
     * @param lastName
     * @param date
     * @param charact
     */
    public void updateUserCharacteristics(ContentResolver contentResolver, long userId,
                                          String userImagePath, String firstName, String lastName, String date, String charact) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Users.USER_IMAGE, userImagePath);
        contentValues.put(Users.FIRST_NAME, firstName);
        contentValues.put(Users.LAST_NAME, lastName);
        contentValues.put(Users.BIRTH_DATE, date);
        contentValues.put(Users.CHARACTERISTICS, charact);
        contentResolver.update(Users.CONTENT_URI, contentValues, Users.USER_ID + "='" + userId + "'", null);
    }

    /**
     * Update a data with selected
     *
     * @param contentResolver
     * @param userId
     * @param selected
     */
    public void updateUserCustomizeSelected(ContentResolver contentResolver, long userId, boolean selected) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Users.CUSTOMIZE_SELECTED, selected);
        contentResolver.update(Users.CONTENT_URI, contentValues, Users.USER_ID + "='" + userId + "'", null);
    }

    /**
     * @param contentResolver
     * @param userId
     * @param selected
     */
    public void ChangeSelectedUserState(ContentResolver contentResolver, long userId, boolean selected) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Users.CUSTOMIZE_SELECTED, selected);
        contentResolver.update(Users.CONTENT_URI, contentValues, Users.USER_ID + "='" + userId + "'", null);

        contentValues.put(Users.CUSTOMIZE_SELECTED, !selected);
        contentResolver.update(Users.CONTENT_URI, contentValues, Users.USER_ID + "!='" + userId + "'", null);
    }

    /**
     * Erases all entries in the database
     *
     * @param contentResolver
     */
    public void refreshCache(ContentResolver contentResolver) {
        int delete = contentResolver.delete(Users.CONTENT_URI, null, null);
        System.out.println("DELETED " + delete + " RECORDS FROM USERS DB");
    }

    /**
     * User data query parameters. Projection columns from indexes defined
     * according to projection.
     */
    public interface UserDataQuery {
        /* Here we define columns which we need */
        String[] PROJECTION = {
                Users.USER_ID,
                Users.USER_IMAGE,
                Users.FIRST_NAME,
                Users.LAST_NAME,
                Users.BIRTH_DATE,
                Users.CHARACTERISTICS,
                Users.CUSTOMIZE_SELECTED,
        };

        /* Here we define columns indexes according to PROJECTION */
        int USER_ID = 0;
        int USER_IMAGE = 1;
        int FIRST_NAME = 2;
        int LAST_NAME = 3;
        int BIRTH_DATE = 4;
        int CHARACTERISTICS = 5;
        int CUSTOMIZE_SELECTED = 6;
    }
}
