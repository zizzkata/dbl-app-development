package com.example.dbl_app_dev;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Contains the information displayed in an accommodation card
 */
public class AccommodationInfo {
    private final ArrayList<Bitmap> photos;
    private final Bitmap photoPanoramic;

    private final String addressTxt;
    private final String floorTxt;
    private final String postcodeTxt;
    private final String priceTxt;
    private final String accommTypeTxt;
    private final String utilitiesTxt;
    private final String areaTxt;
    private final String furnishedTxt;
    private final String petsTxt;
    private final String smokersTxt;
    private final String minimumPeriodTxt;
    private final String availableFromTxt;
    private final String availableUntilTxt;
    private final String descriptionTxt;

    public AccommodationInfo(ArrayList<String> s, ArrayList<Bitmap> photos, Bitmap photoPanoramic) {
        addressTxt = s.get(0);
        floorTxt = s.get(1);
        postcodeTxt = s.get(2);
        priceTxt = s.get(3);
        accommTypeTxt = s.get(4);
        utilitiesTxt = s.get(5);
        areaTxt = s.get(6);
        furnishedTxt = s.get(7);
        petsTxt = s.get(8);
        smokersTxt = s.get(9);
        minimumPeriodTxt = s.get(10);
        availableFromTxt = s.get(11);
        availableUntilTxt = s.get(12);
        descriptionTxt = s.get(13);

        this.photos = new ArrayList<>(photos);
        this.photoPanoramic = photoPanoramic;
    }

    public ArrayList<Bitmap> getPhotos() {
        return photos;
    }

    public Bitmap getPhotoPanoramic() {
        return photoPanoramic;
    }

    public ArrayList<String> getCardFormattedText() {
        ArrayList<String> result = new ArrayList<>();

        result.add(addressTxt);
        result.add(floorTxt);
        result.add(postcodeTxt);
        result.add(priceTxt);
        result.add(accommTypeTxt);
        result.add(utilitiesTxt);
        result.add(areaTxt);
        result.add(furnishedTxt);
        result.add(petsTxt);
        result.add(smokersTxt);
        result.add(minimumPeriodTxt);
        result.add(availableFromTxt);
        result.add(availableUntilTxt);
        result.add(descriptionTxt);

        return result;
    }
}
