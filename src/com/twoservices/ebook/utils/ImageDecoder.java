/**
 *
 * ImageDecoder.java
 * com.twoservices.ebook.utils
 *
 * Created by Ry on 5/31/13.
 * Copyright (c) 2013 Ry. All rights reserved.
 *
 */

package com.twoservices.ebook.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ImageDecoder {

    /**
     * Decode bitmap image from encoded image by BASE64
     *
     * @param encoded buffer encoded buffer by BASE64
     * @return decoded buffer
     */
    public static Bitmap decodeImage(byte[] encodedBuffer) {
        byte[] decodedBuffer = Base64.decode(encodedBuffer, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBuffer, 0, decodedBuffer.length);
    }
}
