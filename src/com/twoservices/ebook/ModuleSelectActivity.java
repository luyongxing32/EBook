/**
 *
 * ModuleSelectActivity.java
 * com.twoservices.ebook
 *
 * Created by Ry on 5/27/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.twoservices.ebook.providers.EBook;
import com.twoservices.ebook.providers.ModuleTable;

public class ModuleSelectActivity extends ListActivity {

    static final String TAG = ModuleSelectActivity.class.getSimpleName();

    public static final String ARG_CHAPTER_INDEX = "chapter_index";
    public static final String ARG_TOPIC_INDEX = "topic_index";
    public static final String ARG_TOPIC_TITLE = "topic_title";

    public static final String ARG_MODULE_ID = "module_id";
    public static final String ARG_MODULE_TITLE = "module_title";

    public static final int MODULE_ID_TAG = R.layout.module_item;
    public static final int MODULE_TITLE_TAG = R.id.text_module_title;

    private int mCurrentChapterIndex;
    private int mCurrentTopicIndex;
    private String mCurrentTopicTitle = "";

    private ModuleTable mModuleTable;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            mCurrentChapterIndex = intent.getIntExtra(ARG_CHAPTER_INDEX, -1);
            mCurrentTopicIndex = intent.getIntExtra(ARG_TOPIC_INDEX, -1);
            mCurrentTopicTitle = intent.getStringExtra(ARG_TOPIC_TITLE);
        }

        // Setup Action Bar
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(String.format("%s.%s %s",
                mCurrentChapterIndex, mCurrentTopicIndex, mCurrentTopicTitle));

        // Open ModuleTable from Database
        mModuleTable = ModuleTable.getInstance();
        mCursor = mModuleTable.getModuleData(getContentResolver(),
                EBook.Module.MODULE_TOPIC_ID + "=" + mCurrentTopicIndex, null);

        if (mCursor != null && mCursor.getCount() > 0) {
            startManagingCursor(mCursor);

            // Create an array adapter for the list view, using mCursor
            setListAdapter(new ModuleCursorAdapter(this, R.layout.module_item, mCursor,
                    new String[] { EBook.Module.MODULE_THUMB_ID, EBook.Module.MODULD_TITLE },
                    new int[] { R.id.image_thumb, R.id.text_module_title }) );
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = new Intent(this, EBookActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.from(this).addNextIntent(upIntent).startActivities();
                    finish();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // TODO: Start TemplateActivity with id of selected template
        if (v.getTag(MODULE_ID_TAG) != null) {
            Integer moduleId = (Integer) v.getTag(MODULE_ID_TAG);
            String moduleTitle = (String) v.getTag(MODULE_TITLE_TAG);

            Intent intent = new Intent(this, TemplateActivity.class);
            intent.putExtra(ARG_CHAPTER_INDEX, mCurrentChapterIndex);
            intent.putExtra(ARG_TOPIC_INDEX, mCurrentTopicIndex);
            intent.putExtra(ARG_TOPIC_TITLE, mCurrentTopicTitle);
            intent.putExtra(ARG_MODULE_ID, moduleId.intValue());
            intent.putExtra(ARG_MODULE_TITLE, moduleTitle);

            startActivity(intent);
        }
    }
}
