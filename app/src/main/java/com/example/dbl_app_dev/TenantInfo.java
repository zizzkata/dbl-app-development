package com.example.dbl_app_dev;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds the information of a Tenant card
 */
public class TenantInfo {
    private String description;
    private String name;
    private List<Bitmap> photos;
    public int age;

    public TenantInfo(String name, String description, List<Bitmap> photos, int age) {
        this.description = description;
        this.photos = new ArrayList<>(photos);
        this.age = age; // could also be moved to the description string
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public List<Bitmap> getPhotos() {
        return photos;
    }
}
