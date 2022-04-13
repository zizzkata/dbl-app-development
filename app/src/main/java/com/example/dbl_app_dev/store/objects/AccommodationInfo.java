package com.example.dbl_app_dev.store.objects;

import android.graphics.Bitmap;

import com.example.dbl_app_dev.network_communication.Database;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private Boolean furnished = false;
    private Boolean pets = false;
    private Boolean smokers = false;

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

    public AccommodationInfo(String ownerUsername, String address, String city, String description
            , String availableUntil, String availableFrom, String floor, Boolean furnished
            , Boolean pets, Boolean smokers, String houseNumber, String minimumPeriod
            , String postcode, Long price, Long area_m2, Bitmap panorama, ArrayList<Bitmap> photos) {
        //documentSnapshot = ds;
        //accommodationId = ds.getId(); // indicates that it doesn't exist in db
        this.address = address;
        this.active = true;
        this.city = city;
        this.currency = "EUR";
        this.description = description;
        this.availableUntil = availableUntil;
        this.availableFrom = availableFrom;
        this.floor = floor;
        this.furnished = furnished;
        this.houseNumber = houseNumber;
        this.minimumPeriod = minimumPeriod;
        this.ownerUsername = ownerUsername;
        this.pets = pets;
        this.postcode = postcode;
        this.price = price;
        this.area_m2 = area_m2;
        this.smokers = smokers;
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

    public String getAddressShort() {
        if (address == null) return "";
        if (address.length() == 0) return "";
        String s = address + ",";
        return s.substring(0, s.indexOf(","));
    }

    public String getAccommodationId() {
        return accommodationId;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getFloor() {
        return floor;
    }

    public String getCurrency() {
        if (currency.equals("EUR")) return "€";

        return currency;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public Boolean getFurnished() {
        if (furnished == null) return false;
        return furnished;
    }

    public Boolean getPets() {
        if (pets == null) return false;
        return pets;
    }

    public Boolean getSmokers() {
        if (smokers == null) return false;
        return smokers;
    }

    public Long getPrice() {
        return price;
    }

    public boolean getActive() {
        return active;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getAreaString() {
        return area_m2.toString();
    }

    public Long getArea() {
        return area_m2;
    }

    public String getMinimumPeriod() {
        return minimumPeriod;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public String getAvailableUntil() {
        return availableUntil;
    }

    public int getPhotosSize() {
        return photos.size();
    }

    // Positive = 1; Neutral = 0; Negative = -1;
    // public int getRating() { return 0;}

    @Deprecated
    public void rateAccommodation(String username, boolean rate) throws Exception {
        //Database.createRatingAccommodation(accommodationId, username, ownerUsername, rate);
    }

    public void setActive(boolean b) { this.active = b; }

    public void setAddress(String s) {
        this.address = s;
    }

    public void setCity(String s) {
        this.city = s;
    }

    public void setCurrency(String s) {
        this.currency = s;
    }

    public void setDescription1(String s) {
        this.description = s;
    }

    public void setAvailableUntil(String s) {
        this.availableUntil = s;
    }

    public void setAvailableFrom(String s) {
        this.availableFrom = s;
    }

    public void setFloor(String s) {
        this.floor = s;
    }

    public void setFurnished(boolean b) {
        this.furnished = b;
    }

    public void setHouseNumber(String s) {
        this.houseNumber = s;
    }

    public void setMinimumPeriod(String s) {
        this.minimumPeriod = s;
    }

    public void setOwnerUsername(String s) {
        this.ownerUsername = s;
    }

    public void setPets1(boolean b) {
        this.pets = b;
    }

    public void setPostcode(String s) {
        this.postcode = s;
    }

    public void setPrice(Long l) {
        this.price = l;
    }

    public void setArea_m2(Long l) {
        this.area_m2 = l;
    }

    public void setSmokers(boolean b) {
        this.smokers = b;
    }

    private Map<String, Object> transformToHash() {
        Map<String, Object> data = new HashMap<>();
        data.put("address", this.address);
        data.put("active", this.active);
        data.put("city", this.city);
        data.put("currency", this.currency);
        data.put("description", this.description);
        data.put("end_date", this.availableUntil);
        data.put("start_date", this.availableFrom);
        data.put("floor", this.floor);
        data.put("furnished", this.furnished);
        data.put("house_number", this.houseNumber);
        data.put("minimum_rent_period", this.minimumPeriod);
        data.put("owner_username", this.ownerUsername);
        data.put("pets", this.pets);
        data.put("post_code", this.postcode);
        data.put("price", this.price);
        data.put("size_m2", this.area_m2);
        data.put("smokers", this.smokers);
        return data;
    }

    /**
     * Creates a new instance of accommodation in the database,
     * or updates it if it already exists.
     * @throws Exception
     */
    public void pushAccommodation() throws Exception {
        // TODO check for anonymous owners.
        Map<String, Object> data = transformToHash();
        if (accommodationId == null || accommodationId == "") { // create new file
            // TODO
        } else { //update accommodation
            //TODO
        }
    }
}
