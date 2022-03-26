package com.example.dbl_app_dev;

import android.graphics.Bitmap;
import android.media.Image;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the information displayed in an accommodation card
 */
public class AccommodationInfo {
    private int currentPhoto;
    private String description;
    private String name;
    private List<Bitmap> photos;
    private Bitmap photoPanoramic;
    public int rent;

    public AccommodationInfo(String name, String description, List<Bitmap> photos, Bitmap photoPanoramic, int rent) {
        this.description = description;
        this.photos = new ArrayList<>(photos);
        this.rent = rent;
        this.name = name;
        this.photoPanoramic = photoPanoramic;
        this.currentPhoto = 0;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getCurrentPhoto() {
        assert photos != null;
        return photos.get(currentPhoto);
    }

    public void incrementPhotoIndex() { currentPhoto++; }

    public void decrementPhotoIndex() { currentPhoto--; }

    public List<Bitmap> getPhotos() {
        return photos;
    }

    public Bitmap getPhotoPanoramic() { return photoPanoramic; }

    public int getRent() { return rent; }

    public String getFormattedName() {
        return name + '\n' + rent + " EUR p/m, excl.";
    }
}
