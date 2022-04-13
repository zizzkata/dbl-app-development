package com.example.dbl_app_dev.util;

import android.graphics.Bitmap;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Tools {
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

    public static ArrayList<Object> getListParameterFromCollection(String param
            , ArrayList<DocumentSnapshot> documentSnapshots) {
        ArrayList<Object> arrayList = new ArrayList<>();
        for (DocumentSnapshot ds : documentSnapshots) {
            if (ds.contains(param)) {
                arrayList.add(ds.get(param));
            }
        }
        return arrayList;
    }

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
