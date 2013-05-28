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
package com.twoservices.ebook;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import com.twoservices.ebook.fragments.ChapterFragment;
import com.twoservices.ebook.fragments.MacroAreaFragment;

public class EBookActivity extends FragmentActivity
        implements MacroAreaFragment.OnMacroAreaSelectedListener {

    private static final String TAG = EBookActivity.class.getSimpleName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_chapters);

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of MacroAreaFragment
            MacroAreaFragment firstFragment = new MacroAreaFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    public void onMacroAreaSelected(int position, int forecolor, int backcolor) {
        // The user selected the MacroArea from the MacroAreaFragment

        // Capture the article fragment from the activity layout
        ChapterFragment chapterFragment = (ChapterFragment)
                getSupportFragmentManager().findFragmentById(R.id.chapter_fragment);

        if (chapterFragment != null) {
            // If chapter fragment is available, we're in two-pane layout...

            // Call a method in the ChapterFragment to update its content
            chapterFragment.updateChapterView(position, forecolor, backcolor);

        } else {
            // If chapter fragment is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            ChapterFragment newFragment = new ChapterFragment();
            Bundle args = new Bundle();
            args.putInt(ChapterFragment.ARG_POSITION, position);
            args.putInt(ChapterFragment.ARG_FORE_COLOR, forecolor);
            args.putInt(ChapterFragment.ARG_BACK_COLOR, backcolor);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }
}
