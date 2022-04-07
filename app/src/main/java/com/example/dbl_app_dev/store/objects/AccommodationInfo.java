package com.example.dbl_app_dev.store.objects;

import android.graphics.Bitmap;

import com.example.dbl_app_dev.network_communication.Database;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

/**
 * Contains the information displayed in an accommodation
 */
public class AccommodationInfo {

    private String accommodationId;
    private boolean active;
    private String ownerUsername;
    private User owner;
    private ArrayList<Bitmap> photos;
    private Bitmap photoPanoramic;
    private DocumentSnapshot documentSnapshot;

    private String address;
    private String city;
    private String floor;
    private String houseNumber;
    private String postcode;
    private Long price;
    private String currency;
    private String accommType;
    private String utilities;
    private Long area_m2;
    private String minimumPeriod;
    private String availableFrom;
    private String availableUntil;
    private String description;

    private Boolean furnished;
    private Boolean pets;
    private Boolean smokers;

    public AccommodationInfo(ArrayList<String> s, ArrayList<Bitmap> photos, Bitmap photoPanoramic) {
        this.accommodationId = "XAFxPJEMgTIRA4HUam4x";


        //this.photos = new ArrayList<>(photos);
        //this.photoPanoramic = photoPanoramic;
    }

    public AccommodationInfo(DocumentSnapshot ds) {
        documentSnapshot = ds;
        accommodationId = ds.getId();
        address = (String) ds.get("address");
        active = (Boolean) ds.get("active");
        city = (String) ds.get("city");
        currency = (String) ds.get("currency");
        description = (String) ds.get("description");
        availableUntil = (String) ds.get("end_date");
        availableFrom = (String) ds.get("start_date");
        floor = (String) ds.get("floor");
        furnished = (Boolean) ds.get("furnished");
        houseNumber = (String) ds.get("house_number");
        minimumPeriod = (String) ds.get("minimum_rent_period");
        ownerUsername = (String) ds.get("owner_username");
        pets = (Boolean) ds.get("pets");
        postcode = (String) ds.get("post_code");
        price = (Long) ds.get("price");
        area_m2 = (Long) ds.get("size_m2");
        smokers = (Boolean) ds.get("smokers");
    }

    /**
     * @return
     */
    public ArrayList<Bitmap> getPhotos() {
        if (photos == null) {
            try {
                this.photos = Database.getStaticImagesBitmaps(this.accommodationId);
            } catch (Exception e) {
                // Doesnt exist
                e.printStackTrace();
            }
        }
        return photos;
    }

    /**
     * @return
     */
    public Bitmap getPhotoPanoramic() {
        if (photoPanoramic == null) {
            try {
                photoPanoramic = Database.getPanoramicImage(this.accommodationId);
            } catch (Exception e) {
                // Doesn't exist
                e.printStackTrace();
            }
        }
        return photoPanoramic;
    }

    /**
     * @return
     */
    public User getOwner() {
        if (owner == null) {
            try {
                owner = new User(Database.getUserInformation(ownerUsername));
            } catch (Exception e) {
                // Anonymous
                e.printStackTrace();
            }
        }
        return owner;
    }

    public DocumentSnapshot getSnapshot() {
        return documentSnapshot;
    }

    public String getAddress() {
        return address;
    }

    public String getAccommodationId() {
        return accommodationId;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return floor;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getFloor() {
        return floor;
    }

    public String getCurrency() {
        return currency;
    }

    public Boolean getFurnished() {
        return furnished;
    }

    public Boolean getPets() {
        return pets;
    }

    public Boolean getSmokers() {
        return smokers;
    }

    public Long getPrice() {
        return price;
    }

    public String getPostcode() { return postcode; }

    public String getAreaString() { return area_m2.toString(); }

    public String getMinimumPeriod() { return minimumPeriod; }

    public String getAvailableFrom() { return availableFrom; }

    public String getAvailableUntil() { return availableUntil; }

    public void rateAccommodation(String username, boolean rate) throws Exception {
        Database.rateAccommodation(accommodationId, username, rate);
    }
}
