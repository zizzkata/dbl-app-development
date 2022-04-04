package com.example.dbl_app_dev;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ImageViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Bitmap> imageBitmaps;
    Bitmap panoramicBitmap;

    public ImageViewPagerAdapter(FragmentManager fm, ArrayList<Bitmap> imageBitmaps, Bitmap panoramicBitmap) {
        super(fm);
        this.panoramicBitmap = panoramicBitmap;
        this.imageBitmaps = imageBitmaps;
        Log.d("extra_debug", "ImageViewPagerAdapter Created");
    }

    @Override
    public int getCount() {
        return imageBitmaps.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ARGalleryFragment.newInstance(panoramicBitmap);
        } else {
            return ImageGalleryFragment.newInstance(imageBitmaps.get(position));
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Child Fragment " + position;
    }
}
