/**
 *
 * MacroAreaTable.java
 * com.twoservices.ebook.providers
 *
 * Created by Ry on 5/26/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook.providers;

import android.content.ContentResolver;
import android.database.Cursor;

/**
 * Database operation wrapper class
 */
public class MacroAreaTable {

    private static final String TAG = MacroAreaTable.class.getSimpleName();
    private static final MacroAreaTable instance = new MacroAreaTable();

    /**
     * Private constructor
     */
    private MacroAreaTable() {
    }

    /**
     * Gets a instance of MacroAreaTable
     */
    public static MacroAreaTable getInstance() {

        return instance;
    }

    /**
     * Gets data of all area
     *
     * @param contentResolver
     * @return
     */
    public Cursor getAreasData(ContentResolver contentResolver, String selection, String[] selectionArgs) {
        return contentResolver.query(MacroAreas.MacroArea.CONTENT_URI, AreaDataQuery.PROJECTION, selection, selectionArgs, null);
    }

    /**
     * MacroAreas data query parameters. Projection columns from indices defined
     * according to projection.
     */
    public interface AreaDataQuery {
        /* Here we define columns which we need */
        String[] PROJECTION = {
                MacroAreas.MacroArea.AREA_ID + " AS _id",
                MacroAreas.MacroArea.AREA_FGCOLOR,
                MacroAreas.MacroArea.AREA_BGCOLOR,
                MacroAreas.MacroArea.AREA_TITLE
        };

        /* Here we define columns indices according to PROJECTION */
        int AREA_ID = 0;
        int AREA_FGCOLOR = 1;
        int ARER_BGCOLOR = 2;
        int AREA_TITLE = 3;
    }
}
