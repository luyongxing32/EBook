/**
 *
 * MacroAreaCursorAdapter.java
 * com.twoservices.ebook.fragments
 *
 * Created by Ry on 5/27/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook.fragments;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.twoservices.ebook.providers.MacroAreaTable;

public class MacroAreaCursorAdapter extends SimpleCursorAdapter {

    Context context;
    Cursor c;
    int layout;
    static TextView row = null;

    public MacroAreaCursorAdapter(Context context, int layout, Cursor c,
                                  String[] from, int[] to) {
        super(context, layout, c, from, to);

        this.context = context;
        this.c = c;
        this.layout = layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            row = (TextView) View.inflate(context, layout, null);
        } else {
            row = (TextView) convertView;
        }

        if (c != null) {
            c.moveToPosition(position);

            row.setText(c.getString(MacroAreaTable.AreaDataQuery.AREA_TITLE));
            row.setTextColor(c.getInt(MacroAreaTable.AreaDataQuery.AREA_FGCOLOR));
            row.setBackgroundColor(c.getInt(MacroAreaTable.AreaDataQuery.ARER_BGCOLOR));
        }

        return row;
    }

}