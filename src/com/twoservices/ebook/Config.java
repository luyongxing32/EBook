/**
 *
 * Config.java
 * com.twoservices.ebook
 *
 * Created by Ry on 5/26/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Config {

    public static final boolean DEBUG = true;

    public static final String PREF_AREA_INDEX = "macroarea_index";
    public static final String PREF_FORE_COLOR = "forecolor";
    public static final String PREF_BACK_COLOR = "backcolor";

    private static final int DEFAULT_AREA_INDEX = 0;
    private static final int DEFAULT_FORE_COLOR = 0;
    private static final int DEFAULT_BACK_COLOR = 0xFFFFFF;

    public static int sCurrentAreaIndex;
    public static int sCurrentForeColor;
    public static int sCurrentBackColor;

    /**
     * Load information of last selected macroarea
     *
     * @param context
     */
    public static void loadLastSelectedArea(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        sCurrentAreaIndex = prefs.getInt(PREF_AREA_INDEX, DEFAULT_AREA_INDEX);
        sCurrentForeColor = prefs.getInt(PREF_FORE_COLOR, DEFAULT_FORE_COLOR);
        sCurrentBackColor = prefs.getInt(PREF_BACK_COLOR, DEFAULT_BACK_COLOR);
    }

    /**
     * Save information of last selected macroarea
     *
     * @param context
     */
    public static void saveLastSelectedArea(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(PREF_AREA_INDEX, sCurrentAreaIndex);
        editor.putInt(PREF_FORE_COLOR, sCurrentForeColor);
        editor.putInt(PREF_BACK_COLOR, sCurrentBackColor);

        editor.commit();
    }
}
