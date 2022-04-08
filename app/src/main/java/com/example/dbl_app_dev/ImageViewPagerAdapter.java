package com.example.dbl_app_dev;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ImageViewPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Bitmap> imageBitmaps;
    private final Bitmap panoramicBitmap;
    private long baseId = 0;

    public ImageViewPagerAdapter(FragmentManager fm, ArrayList<Bitmap> imageBitmaps, @NonNull Bitmap panoramicBitmap) {
        super(fm);
        this.panoramicBitmap = panoramicBitmap;
        this.imageBitmaps = imageBitmaps;
        Log.d("extra_debug", "ImageViewPagerAdapter Created");
        Log.d("test", ((Integer) imageBitmaps.size()).toString());
    }

    @Override
    public int getCount() {
        return imageBitmaps.size() + 1;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ARGalleryFragment.newInstance(panoramicBitmap);
        } else {
            Log.d("test", "creating new image fragment");
            return ImageGalleryFragment.newInstance(imageBitmaps.get(position - 1));
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Child Fragment " + position;
    }

    @Override
    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public long getItemId(int position) {
        // give an ID different from position when position has been changed
        return baseId + position;
    }

    public void notifyChangeInPosition(int n) {
        // shift the ID returned by getItemId outside the range of all previous fragments
        baseId += getCount() + n;
    }
}
