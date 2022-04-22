package com.example.dbl_app_dev.util.adapters;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.dbl_app_dev.ARGalleryFragment;
import com.example.dbl_app_dev.ImageGalleryFragment;

import java.util.ArrayList;

/**
 * Adapter class for verticalViewPager.
 * It handles data processing for one panoramic bitmap, and a list of normal (2D) bitmaps
 */
public class ImageViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Bitmap> imageBitmaps;
    private Bitmap panoramicBitmap;
    // baseId is used to make sure that all fragments (even across different accommodations)
    // will have unique id's
    private long baseId = 0;

    public ImageViewPagerAdapter(FragmentManager fm, ArrayList<Bitmap> imageBitmaps, @NonNull Bitmap panoramicBitmap) {
        super(fm);
        this.panoramicBitmap = panoramicBitmap;
        this.imageBitmaps = imageBitmaps;
        Log.d("extra_debug", "ImageViewPagerAdapter Created");
    }

    /**
     * @return the number of data views contained in this adapter
     *
     * (corresponds to the total number of Fragments inside the VerticalViewPager)
     */
    @Override
    public int getCount() {
        return imageBitmaps.size() + 1;
    }

    /**
     * @param position index of fragment to be retrieved
     * @return fragment that corresponds to the data at the given index
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ARGalleryFragment.newInstance(panoramicBitmap);
        } else {
            return ImageGalleryFragment.newInstance(imageBitmaps.get(position - 1));
        }
    }

    /**
     * @param position given index
     * @return a textual representation of the Fragment/data at that position
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return "Child Fragment " + position;
    }

    /**
     * used to refresh all fragments when data set changed
     */
    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    /**
     * used to give an ID different from position when position has been changed
     */
    @Override
    public long getItemId(int position) {
        return baseId + position;
    }

    /**
     * @param n the shift to be applied to baseID
     *
     * Will shift the ID returned by getItemId outside the range of all previous fragments
     */
    public void notifyChangeInPosition(int n) {
        baseId += getCount() + 53;
    }

    public void setImageBitmaps(ArrayList<Bitmap> bitmaps) {
        imageBitmaps = bitmaps;
    }

    public void setPanoramicBitmap(Bitmap bitmap) {
        panoramicBitmap = bitmap;
    }
}
