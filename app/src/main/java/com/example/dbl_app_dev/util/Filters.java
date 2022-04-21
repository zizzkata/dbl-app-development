package com.example.dbl_app_dev.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dbl_app_dev.store.objects.AccommodationInfo;


/**
 * Class that stores the filters of the Tenant Discovery page.
 */
public class Filters {
    private boolean isFurnished;
    private Long priceUpper;
    private Long priceLower;
    private Long areaUpper;
    private Long areaLower;
    private String city;

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
        if (!(city.equalsIgnoreCase(accommodation.getCity()))) return false;

        return true;
    }

    public boolean isFurnished() {
        return isFurnished;
    }

    public void setFurnished(boolean furnished) {
        isFurnished = furnished;
    }

    public void setPriceUpper(Long priceUpper) {
        this.priceUpper = priceUpper;
    }

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

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setAreaLower(Long areaLower) {
        this.areaLower = areaLower;
    }

    public void setAreaUpper(Long areaUpper) {
        this.areaUpper = areaUpper;
    }

    public Long getAreaUpper() {
        return areaUpper;
    }

    public Long getAreaLower() {
        return areaLower;
    }

    public Long getPriceUpper() {
        return priceUpper;
    }

    public Long getPriceLower() {
        return priceLower;
    }
}
