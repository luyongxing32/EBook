/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twoservices.ebook.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.twoservices.ebook.Config;
import com.twoservices.ebook.R;
import com.twoservices.ebook.providers.ChapterTable;
import com.twoservices.ebook.providers.EBook;

public class ChapterFragment extends ListFragment {

    static final String TAG = ChapterFragment.class.getSimpleName();

    public final static String ARG_POSITION = "position";
    public final static String ARG_FORE_COLOR = "forecolor";
    public final static String ARG_BACK_COLOR = "backcolor";

    int mCurrentPosition;
    int mCurrentForeColor;
    int mCurrentBackColor;

    private ChapterTable mChapterTable;
    private Cursor mCursor;
    private ChapterCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If activity recreated (such as from screen rotate), restore
        // the previous chapter selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
            mCurrentForeColor = savedInstanceState.getInt(ARG_FORE_COLOR);
            mCurrentBackColor = savedInstanceState.getInt(ARG_BACK_COLOR);
        } else {
            mCurrentPosition = Config.sCurrentAreaIndex;
            mCurrentForeColor = Config.sCurrentForeColor;
            mCurrentBackColor = Config.sCurrentBackColor;
        }

        // Open ChapterTable from Database
        mChapterTable = ChapterTable.getInstance();

        // Get cursor with the position of selected area
        mCursor = mChapterTable.getChapterData(getActivity().getContentResolver(),
                EBook.Chapter.CHAPTER_AREA_ID + "=" + String.format("%s", mCurrentPosition + 1), null);

        getActivity().startManagingCursor(mCursor);

        // Create an array adapter for the list view, using mCursor
        mAdapter = new ChapterCursorAdapter(getActivity(), R.layout.chapter_item,
                mCursor, new String[]{EBook.Chapter.CHAPTER_TITLE},
                new int[]{R.id.text_chapter_title}, mCurrentBackColor, mCurrentForeColor);
        mAdapter.setForeColor(mCurrentForeColor);
        mAdapter.setBackColor(mCurrentBackColor);

        setListAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set chapter based on argument passed in
            updateChapterView(args.getInt(ARG_POSITION), args.getInt(ARG_FORE_COLOR),
                    args.getInt(ARG_BACK_COLOR));
        } else if (mCurrentPosition != -1) {
            // Set chapter based on saved instance state defined during onCreateView
            updateChapterView(mCurrentPosition, mCurrentForeColor, mCurrentBackColor);
        }
    }

    public void updateChapterView(int position, int forecolor, int backcolor) {
        mCurrentPosition = position;
        mCurrentForeColor = forecolor;
        mCurrentBackColor = backcolor;

        mAdapter.setForeColor(forecolor);
        mAdapter.setBackColor(backcolor);

        // Get cursor with the position of selected area
        mCursor = mChapterTable.getChapterData(getActivity().getContentResolver(),
                EBook.Chapter.CHAPTER_AREA_ID + "=" + String.format("%s", mCurrentPosition + 1), null);

        getActivity().startManagingCursor(mCursor);

        // Create an array adapter for the list view, using mCursor
        mAdapter = new ChapterCursorAdapter(getActivity(), R.layout.chapter_item,
                mCursor, new String[]{EBook.Chapter.CHAPTER_TITLE},
                new int[]{R.id.text_chapter_title}, mCurrentBackColor, mCurrentForeColor);

        setListAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current macroarea selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
        outState.putInt(ARG_FORE_COLOR, mCurrentForeColor);
        outState.putInt(ARG_BACK_COLOR, mCurrentBackColor);
    }
}
