package com.example.dbl_app_dev;

import android.graphics.Bitmap;

/**
 * Contains the information displayed in a tenant card
 */
public class TenantInfo {
    private String description;
    private String name;
    private Bitmap photo;
    public int age;

    public TenantInfo(String name, String description, Bitmap photo, int age) {
        this.description = description;
        this.photo = photo;
        this.age = age; // could also be moved to the description string
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPhoto() {
        return photo;
    }
}
