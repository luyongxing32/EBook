/**
 *
 * ModuleCursorAdapter.java
 * com.twoservices.ebook.fragments
 *
 * Created by Ry on 5/27/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twoservices.ebook.providers.MediaTable;
import com.twoservices.ebook.providers.ModuleTable;
import com.twoservices.ebook.utils.ImageDecoder;

public class ModuleCursorAdapter extends SimpleCursorAdapter {

    Context context;
    int layout;
    Cursor mModuleCursor;

    MediaTable mMediaTable;
    Cursor mMediaCursor;
    ContentResolver mContentResolver;

    public ModuleCursorAdapter(Context context, int layout, Cursor c,
                                  String[] from, int[] to) {
        super(context, layout, c, from, to);

        this.context = context;
        this.layout = layout;
        this.mModuleCursor = c;

        mMediaTable = MediaTable.getInstance();
        mContentResolver = context.getContentResolver();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        if (convertView == null) {
            convertView = View.inflate(context, layout, null);

            holder = new ViewHolder();
            holder.thumbImageView = (ImageView) convertView.findViewById(R.id.image_thumb);
            holder.moduleTitleText = (TextView) convertView.findViewById(R.id.text_module_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mModuleCursor != null) {
            mModuleCursor.moveToPosition(position);

            final int id = mModuleCursor.getInt(ModuleTable.ModuleDataQuery.MODULE_ID);
            final String moduleTitle = mModuleCursor.getString(ModuleTable.ModuleDataQuery.MODULE_TITLE);

            holder.moduleTitleText.setText(moduleTitle);

            mMediaCursor = mMediaTable.getMediaData(mContentResolver,
                    MediaTable.MetaInfoDataQuery.MEDIA_ID + "=" + id, null);

            if (mMediaCursor != null && mMediaCursor.getCount() > 0) {
                holder.thumbImageView.setImageBitmap(ImageDecoder.decodeImage(mMediaCursor.getBlob(
                        MediaTable.MetaInfoDataQuery.MEDIA_VALUE)));
            }

            convertView.setTag(ModuleSelectActivity.MODULE_ID_TAG, id);
            convertView.setTag(ModuleSelectActivity.MODULE_TITLE_TAG, moduleTitle);
            mMediaCursor.close();
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView thumbImageView;
        TextView moduleTitleText;
    }

}
