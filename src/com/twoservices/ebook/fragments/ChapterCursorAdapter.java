/**
 *
 * ChapterCursorAdapter.java
 * com.twoservices.ebook.fragments
 *
 * Created by Ry on 5/27/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twoservices.ebook.ModuleSelectActivity;
import com.twoservices.ebook.R;
import com.twoservices.ebook.providers.ChapterTable;
import com.twoservices.ebook.providers.EBook;
import com.twoservices.ebook.providers.TopicTable;

public class ChapterCursorAdapter extends SimpleCursorAdapter {

    Context context;
    Cursor mChapterCursor;
    int layout;
    int forecolor;
    int backcolor;
    TopicTable mTopicTable;
    Cursor mTopicCursor;
    ContentResolver mContentResolver;

    public ChapterCursorAdapter(Context context, int layout, Cursor c,
                                String[] from, int[] to, int backcolor, int forecolor) {
        super(context, layout, c, from, to);

        this.context = context;
        this.mChapterCursor = c;
        this.layout = layout;

        this.forecolor = forecolor;
        this.backcolor = backcolor;

        mTopicTable = TopicTable.getInstance();
        mContentResolver = context.getContentResolver();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(context, layout, null);

            holder = new ViewHolder();
            holder.chapterTitleText = (TextView) convertView.findViewById(R.id.text_chapter_title);
            holder.topicContainer = (LinearLayout) convertView.findViewById(R.id.topic_item_container);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mChapterCursor != null) {
            mChapterCursor.moveToPosition(position);

            // Get information of Chapter record
            int id = mChapterCursor.getInt(ChapterTable.ChapterDataQuery.CHAPTER_ID);
            String title = mChapterCursor.getString(ChapterTable.ChapterDataQuery.CHAPTER_TITLE);
            int flags = mChapterCursor.getInt(ChapterTable.ChapterDataQuery.CHAPTER_FLAGS);

            // Set a title of chapter
            holder.chapterTitleText.setText(title);
            holder.chapterTitleText.setTextColor(forecolor);

            // Process topic items
            // First, get cursor of TopicTable
            mTopicCursor = mTopicTable.getTopicData(mContentResolver,
                    EBook.Topic.TOPIC_CHAPTER_ID + "=" + id, null);

            // Add topic items
            holder.topicContainer.removeAllViews();

            if (mTopicCursor != null && mTopicCursor.getCount() > 0) {
                mTopicCursor.moveToFirst();

                int index = 1;
                while (!mTopicCursor.isAfterLast()) {
                    addTopicItemView(holder.topicContainer,
                            mTopicCursor.getInt(TopicTable.TopicDataQuery.TOPIC_CHAPTER_ID), index++,
                            mTopicCursor.getString(TopicTable.TopicDataQuery.TOPIC_TITLE));

                    mTopicCursor.moveToNext();
                }

                mTopicCursor.close();
            }

        }

        convertView.setBackgroundColor(backcolor);
        return convertView;
    }

    private void addTopicItemView(LinearLayout container, final int chapterIndex, final int topicIndex, final String topicTitle) {

        LinearLayout oneTopicItem = (LinearLayout) View.inflate(context, R.layout.topic_item, null);
        TextView topicNumText = (TextView) oneTopicItem.findViewById(R.id.text_topic_num);
        TextView topicTitleText = (TextView) oneTopicItem.findViewById(R.id.text_topic_title);

        topicNumText.setText(String.format("%s.%s", chapterIndex, topicIndex));
        topicNumText.setTextColor(forecolor);
        topicTitleText.setText(topicTitle);
        topicTitleText.setTextColor(forecolor);

        oneTopicItem.setClickable(true);
        oneTopicItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("ChapterCursorAdapter", "click at (" + chapterIndex + ", " + topicIndex + ")");
                Intent intent = new Intent(context, ModuleSelectActivity.class);
                intent.putExtra(ModuleSelectActivity.ARG_CHAPTER_INDEX, chapterIndex);
                intent.putExtra(ModuleSelectActivity.ARG_TOPIC_INDEX, topicIndex);
                intent.putExtra(ModuleSelectActivity.ARG_TOPIC_TITLE, topicTitle);
                context.startActivity(intent);
            }
        });

        container.addView(oneTopicItem);
    }

    public void setForeColor(int forecolor) {
        this.forecolor = forecolor;
    }

    public void setBackColor(int backcolor) {
        this.backcolor = backcolor;
    }

    static class ViewHolder {
        TextView chapterTitleText;
        LinearLayout topicContainer;
    }
}
