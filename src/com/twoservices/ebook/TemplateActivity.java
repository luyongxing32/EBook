/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twoservices.ebook;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.twoservices.ebook.providers.EBook;
import com.twoservices.ebook.providers.MediaTable;
import com.twoservices.ebook.providers.TemplateTable;
import com.twoservices.ebook.utils.ImageDecoder;

public class TemplateActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
     * derivative, which will destroy and re-create fragments as needed, saving and restoring their
     * state in the process. This is important to conserve memory and is a best practice when
     * allowing navigation between objects in a potentially large collection.
     */
    TemplateCollectionPagerAdapter mTemplateCollectionPagerAdapter;

    private int mCurrentChapterIndex;
    private int mCurrentTopicIndex;
    private String mCurrentTopicTitle = "";
    private int mCurrentModuleIndex;
    private String mCurrentModuleTitle = "";

    TemplateTable mTemplateTable;
    Cursor mTemplateCursor;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_collection);

        Intent intent = getIntent();
        if (intent != null) {
            mCurrentChapterIndex = intent.getIntExtra(ModuleSelectActivity.ARG_CHAPTER_INDEX, -1);
            mCurrentTopicIndex = intent.getIntExtra(ModuleSelectActivity.ARG_TOPIC_INDEX, -1);
            mCurrentTopicTitle = intent.getStringExtra(ModuleSelectActivity.ARG_TOPIC_TITLE);
            mCurrentModuleIndex = intent.getIntExtra(ModuleSelectActivity.ARG_MODULE_ID, -1);
            mCurrentModuleTitle = intent.getStringExtra(ModuleSelectActivity.ARG_MODULE_TITLE);
        }

        // Setup Action Bar
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(String.format("%s - %s", mCurrentTopicTitle, mCurrentModuleTitle));

        // Open ModuleTable from Database
        mTemplateTable = TemplateTable.getInstance();
        mTemplateCursor = mTemplateTable.getTemplateData(getContentResolver(),
                EBook.Template.TEMPLATE_MODULE_ID + "=" + mCurrentModuleIndex, null);

        if (mTemplateCursor != null && mTemplateCursor.getCount() > 0) {
            startManagingCursor(mTemplateCursor);

            // Create an adapter that when requested, will return a fragment representing an object in
            // the collection.
            // ViewPager and its adapters use support library fragments, so we must use
            // getSupportFragmentManager.
            mTemplateCollectionPagerAdapter = new TemplateCollectionPagerAdapter(getSupportFragmentManager(), mTemplateCursor);

            // Set up the ViewPager, attaching the adapter.
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mTemplateCollectionPagerAdapter);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
//                Intent upIntent = new Intent(this, ModuleSelectActivity.class);
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                    // This activity is not part of the application's task, so create a new task
//                    // with a synthesized back stack.
//                    TaskStackBuilder.from(this)
//                            // If there are ancestor activities, they should be added here.
//                            .addNextIntent(upIntent)
//                            .startActivities();
//                    finish();
//                } else {
//                    // This activity is part of the application's task, so simply
//                    // navigate up to the hierarchical parent activity.
//                    NavUtils.navigateUpTo(this, upIntent);
//                }
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public static class TemplateCollectionPagerAdapter extends FragmentStatePagerAdapter {

        Cursor cursor;

        public TemplateCollectionPagerAdapter(FragmentManager fm, Cursor cursor) {
            super(fm);

            this.cursor = cursor;
        }

        @Override
        public Fragment getItem(int i) {
            cursor.moveToPosition(i);

            int templateId = cursor.getInt(TemplateTable.TemplateDataQuery.TEMPLATE_ID);
            String content = cursor.getString(TemplateTable.TemplateDataQuery.TEMPLATE_TEXT);
            String imageIndices = cursor.getString(TemplateTable.TemplateDataQuery.TEMPLATE_IMAGES);

            Fragment fragment = new TemplateFragment();
            Bundle args = new Bundle();
            args.putInt(TemplateFragment.ARG_TEMPLATE_ID, templateId);
            args.putString(TemplateFragment.ARG_TEMPLATE_CONTENT, content);
            args.putString(TemplateFragment.ARG_TEMPLATE_IMAGES, imageIndices);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return cursor.getCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "TEMPLETE " + (position + 1);
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class TemplateFragment extends Fragment {

        public static final String ARG_TEMPLATE_ID = "template_id";
        public static final String ARG_TEMPLATE_CONTENT = "template_content";
        public static final String ARG_TEMPLATE_IMAGES = "template_images";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.template_fragment, container, false);
            Bundle args = getArguments();

            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            String content = args.getString(ARG_TEMPLATE_CONTENT);

            // Setting up WebView for Text content
            WebView webView = (WebView) rootView.findViewById(R.id.webView1);
            webView.loadData(content, mimeType, encoding);
//            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
//            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setDisplayZoomControls(true);
            webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);

            // Setting up View for Image content
            LinearLayout image_container = (LinearLayout) rootView.findViewById(R.id.image_container);
            addImageView(image_container, content);

            return rootView;
        }

        private void addImageView(LinearLayout container, String imageIDs) {

            String[] ids = imageIDs.split(",");

            // Skip process since image id array is empty
            if (ids == null || ids.length == 0) return;

            // Gather image id
            StringBuilder sb = new StringBuilder("rowid IN ( ");
            for (String id : ids) {
                if (!TextUtils.isEmpty(id) && TextUtils.isDigitsOnly(id)) {
                    sb.append(id).append(",");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");

            MediaTable mediaTable = MediaTable.getInstance();
            Cursor cursor = mediaTable.getMediaData(
                    getActivity().getContentResolver(), sb.toString(), null);

            if (cursor != null) {
                container.removeAllViews();
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        Bitmap bitmap = null;
                        bitmap = ImageDecoder.decodeImage(cursor.getBlob(MediaTable.MetaInfoDataQuery.MEDIA_VALUE));

                        // Add an imageView
                        ImageView imageView = new ImageView(this.getActivity());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        layoutParams.setMargins(0, 16, 0, 16);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setImageBitmap(bitmap);

                        container.addView(imageView);

                        if (bitmap != null) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    }
                }

                cursor.close();
            }
        }
    }

}

