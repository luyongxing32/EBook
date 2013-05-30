/**
 *
 * RestoreDatabase.java
 * com.twoservices.ebook.utils
 *
 * Created by Ry on 5/26/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.twoservices.ebook.providers.EBookContentProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RestoreDatabase {

    static final String TAG = RestoreDatabase.class.getSimpleName();

    private static final String ENCODE_FILE_NAME = "content.sql";
    private static final String DATABASE_FILE_NAME = EBookContentProvider.DATABASE_NAME;


    /**
     * Check if specified file is exist
     *
     * @param context Application context
     * @return whether file is exists or not
     */
    public static boolean isExistsFile(Context context) {

        File f = context.getDatabasePath(DATABASE_FILE_NAME);

        if (!f.exists()) {
            File dir = f.getParentFile();
            dir.mkdir();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Decode and Copy database file from assets to private directory
     *
     * @param context Application context for file operation
     * @return whether operation is success or not
     */
    public static boolean extractDB(Context context) {

        // TODO: decode database file


        // copy decoded file to private database directory
        return copyDatabase(context);
    }

    /**
     * Copy database file to private directory
     *
     * @param context Application context for file copying
     * @return whether copy action is success or not
     */
    private static boolean copyDatabase(Context context) {

        AssetManager assetManager = context.getAssets();

        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e(TAG, "Failed to get asset file list.", e);
            return false;
        }

        String filename = context.getDatabasePath(DATABASE_FILE_NAME).getAbsolutePath();
        InputStream in = null;
        OutputStream out = null;

        try {
            in = assetManager.open(ENCODE_FILE_NAME);
            out = new FileOutputStream(filename);

            copyFile(in, out);

            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            Log.e(TAG, "Failed to copy asset file: " + filename, e);
            return false;
        }

        return true;
    }

    /**
     * Copy a file with InputStream and OutputStream
     *
     * @param in  Source of file that would be copied
     * @param out Destination file that would be copied
     * @throws IOException
     */
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}
