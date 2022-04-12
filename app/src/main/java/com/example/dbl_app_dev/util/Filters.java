package com.example.dbl_app_dev.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dbl_app_dev.store.objects.AccommodationInfo;

public class Filters {
    private boolean isFurnished;
    private Long priceUpper;
    private Long priceLower;

    public Filters() {
        this.isFurnished = false;
        this.priceUpper = 0L;
        this.priceLower = 9999L;

        Log.d("debug_extra", "New Filters object created");
    }

    /**
     * @param accommodation a given accommodation
     * @return if the accommodation passes the set filters
     */
    public boolean isOk(AccommodationInfo accommodation) {
        Long price = accommodation.getPrice();

        if (accommodation.getFurnished() && !isFurnished) return false;
        if (!(priceLower < price && price < priceUpper)) return false;
        return true;
    }

    public boolean isFurnished() {
        return isFurnished;
    }

    public void setFurnished(boolean furnished) {
        isFurnished = furnished;
    }

    public Long getPriceUpper() {
        return priceUpper;
    }

    public void setPriceUpper(Long priceUpper) {
        this.priceUpper = priceUpper;
    }

    public Long getPriceLower() {
        return priceLower;
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
                '}';
    }
}
