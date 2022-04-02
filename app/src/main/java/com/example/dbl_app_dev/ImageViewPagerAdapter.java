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

    public ImageViewPagerAdapter(FragmentManager fm, ArrayList<Bitmap> imageBitmaps) {
        super(fm);
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
        return ImageGalleryFragment.newInstance(imageBitmaps.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Child Fragment " + position;
    }
}
