package com.example.dbl_app_dev.util;

import android.graphics.Bitmap;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a number of useful, general-purpose static methods
 */
public class Tools {
    /**
     * @param image a given bitmap to be resized
     * @param maxSize the maximum size (in pixels) that the largest 'edge' of the bitmap can have
     * @return the same bitmap, resized to have its' longest side == maxSize
     */
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    /**
     * @param param a given parameter
     * @param documentSnapshots a list of DocumentSnapshot objects
     * @return an arraylist
     */
    public static <U> ArrayList<U> getListParameterFromCollection(String param
            , List<DocumentSnapshot> documentSnapshots) {
        ArrayList<U> arrayList = new ArrayList<>();
        for (DocumentSnapshot ds : documentSnapshots) {
            if (ds.contains(param)) {
                arrayList.add((U) ds.get(param));
            }
        }
        return arrayList;
    }
}
