package com.example.dbl_app_dev.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dbl_app_dev.store.objects.AccommodationInfo;


/**
 * Class that stores the filters of the Tenant Discovery page.
 */
public class Filters {
    private boolean isFurnished;
    // represents the upper bound of the price filter
    private Long priceUpper;
    // represents the lower bound of the price filter
    private Long priceLower;
    // the upper bound of the area filter
    private Long areaUpper;
    // the lower bound of the area filter
    private Long areaLower;
    private String city;

    /**
     * Default constructor for this class, initializes all variables to sensible values
     */
    public Filters() {
        this.isFurnished = false;
        this.priceUpper = 9999L;
        this.priceLower = 0L;
        this.areaUpper = 9999L;
        this.areaLower = 0L;
        this.city = "";

        Log.d("debug_extra", "New Filters object created");
    }

    /**
     * @param accommodation a given accommodation
     * @return if the accommodation passes the set filters
     */
    public boolean isOk(AccommodationInfo accommodation) {
        Long price = accommodation.getPrice();
        Long area = accommodation.getArea();

        if (accommodation.getFurnished() && !isFurnished) return false;
        if (!(priceLower < price && price < priceUpper)) return false;
        if (!(areaLower < area && area < areaUpper)) return false;
        return city.equalsIgnoreCase(accommodation.getCity());
    }

    // getter for isFurnished
    public boolean isFurnished() {
        return isFurnished;
    }

    // setter for isFurnished
    public void setFurnished(boolean furnished) {
        isFurnished = furnished;
    }

    // setter for PriceUpper
    public void setPriceUpper(Long priceUpper) {
        this.priceUpper = priceUpper;
    }

    // setter for PriceLower
    public void setPriceLower(Long priceLower) {
        this.priceLower = priceLower;
    }

    @NonNull
    @Override
    public String toString() {
        return "Filters{" +
                "isFurnished=" + isFurnished +
                ", priceUpper=" + priceUpper +
                ", priceLower=" + priceLower +
                ", areaUpper=" + areaUpper +
                ", areaLower=" + areaLower +
                ", city='" + city + '\'' +
                '}';
    }

    // setter for city
    public void setCity(String city) {
        this.city = city;
    }

    // getter for city
    public String getCity() {
        return city;
    }

    // setter for areaLower
    public void setAreaLower(Long areaLower) {
        this.areaLower = areaLower;
    }

    // setter for areaUpper
    public void setAreaUpper(Long areaUpper) {
        this.areaUpper = areaUpper;
    }

    // getter for areaUpper
    public Long getAreaUpper() {
        return areaUpper;
    }

    // getter for areaLower
    public Long getAreaLower() {
        return areaLower;
    }

    // getter for priceUpper
    public Long getPriceUpper() {
        return priceUpper;
    }

    // getter for priceLower
    public Long getPriceLower() {
        return priceLower;
    }
}
