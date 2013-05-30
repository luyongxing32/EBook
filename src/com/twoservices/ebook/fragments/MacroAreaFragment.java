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

import android.app.Activity;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.twoservices.ebook.R;
import com.twoservices.ebook.providers.EBook;
import com.twoservices.ebook.providers.MacroAreaTable;

public class MacroAreaFragment extends ListFragment {

    static final String TAG = MacroAreaFragment.class.getSimpleName();

    private MacroAreaTable mAreaTable;
    private Cursor mCursor;

    OnMacroAreaSelectedListener mCallback;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnMacroAreaSelectedListener {
        /**
         * Called by MacroAreaFragment when a list item is selected
         */
        public void onMacroAreaSelected(int position, int forecolor, int backcolor);
    }

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        // Open MacroAreaTable from Database
        mAreaTable = MacroAreaTable.getInstance();
        mCursor = mAreaTable.getAreasData(getActivity().getContentResolver(), null, null);

        if (mCursor != null && mCursor.getCount() > 0) {
            getActivity().startManagingCursor(mCursor);

            // Create an array adapter for the list view, using mCursor
            setListAdapter(new MacroAreaCursorAdapter(getActivity(), layout, mCursor,
                    new String[] { EBook.MacroArea.AREA_TITLE }, new int[] { android.R.id.text1 }) );
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.chapter_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnMacroAreaSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMacroAreaSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        TextView titleText = (TextView) v;
        int forecolor = titleText.getTextColors().getDefaultColor();
        ColorDrawable background = (ColorDrawable) titleText.getBackground();
        int backcolor = background.getColor();

        // Notify the parent activity of selected item
        mCallback.onMacroAreaSelected(position, forecolor, backcolor);

        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
}
