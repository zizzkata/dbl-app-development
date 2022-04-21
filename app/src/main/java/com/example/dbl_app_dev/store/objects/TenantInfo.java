package com.example.dbl_app_dev.store.objects;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Contains the information displayed in a tenant card
 */
public class TenantInfo {
    private final Bitmap photo;

    private final String name;    // contains full name
    private final String email;
    private final String phone;
    private final String smoker;
    private final String petOwner;
    private final String description;
    private final String gender;

    public TenantInfo(ArrayList<String> s, Bitmap photo) {
        this.name = s.get(0);
        this.email = s.get(1);
        this.phone = s.get(2);
        this.smoker = s.get(3);
        this.petOwner = s.get(4);
        this.description = s.get(5);
        this.gender = s.get(6);

        this.photo = photo;
    }

    /**
     * Get an array of the tenant details
     *
     * @return ArrayList with the tenant details
     */
    public ArrayList<String> getFormattedText() {
        ArrayList<String> result = new ArrayList<>();

        result.add(name);
        result.add(email);
        result.add(phone);
        result.add(smoker);
        result.add(petOwner);
        result.add(description);
        result.add(gender);

        return result;
    }

    /**
     * Get the profile picture of the tenant as a bitmap
     *
     * @return profile picture of the tenant
     */
    public Bitmap getPhoto() {
        return photo;
    }
}
