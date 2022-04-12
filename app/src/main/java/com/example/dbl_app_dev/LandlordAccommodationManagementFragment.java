package com.example.dbl_app_dev;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.dbl_app_dev.network_communication.Database;
import com.example.dbl_app_dev.store.Store;
import com.example.dbl_app_dev.store.objects.AccommodationInfo;
import com.example.dbl_app_dev.store.objects.User;
import com.example.dbl_app_dev.util.AsyncWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LandlordAccommodationManagementFragment#newInstance} factory
 * method to
 * create an instance of this fragment.
 */
public class LandlordAccommodationManagementFragment extends Fragment {

    ConstraintLayout accommParent;
    ArrayList<AccommodationInfo> myListings = null;
    ArrayList<TenantAccommodationObject> myListingsObjects = new ArrayList<>();

    public LandlordAccommodationManagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static LandlordAccommodationManagementFragment newInstance() {
        LandlordAccommodationManagementFragment fragment = new LandlordAccommodationManagementFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landlord_accommodation_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Add functionality for add accommodation button
        Button addListingBtn = getView().findViewById(R.id.addListingBtn);
        addListingBtn.setOnClickListener(view1 -> {
            AlertDialog d = ((MainNavigationActivity) getActivity()).createNewAccommodationDialog();
            setCreateButtonFunctionality(d);
        });


        // Get the parent view of the accommodation objects
        accommParent = getView().findViewById(R.id.scrollConstraintLayout);
        accommParent.removeAllViews();

        // Get the liked listings of the user
        AsyncWrapper.wrap(() -> {
            try {
                myListings = Store.getCurrentUserLikedAccommodations();
                getActivity().runOnUiThread(() -> {
                            if (myListings != null) {
                                addMyListings();
                                addAllEditButtonsFunctionality();
                            }
                        }
                );
            } catch (Exception e) {
                Log.e("ERR", e.getMessage());
            }
        });
    }

    private void setCreateButtonFunctionality(AlertDialog ad) {
        //IDK
    }

    private void addMyListings() {
        // Remembers the previously created view
        View previousView = null;
        ConstraintLayout view; // newly created view
        ConstraintLayout.LayoutParams lp; // LayoutParams for newly created view
        // Layout inflater for adding the new view
        LayoutInflater vi = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create the listings
        int i = 0;
        for (AccommodationInfo listing : myListings) {
            if (i == 0) previousView = accommParent;

            // Create the new view and add it to the parent
            view = (ConstraintLayout) vi
                    .inflate(R.layout.compact_accommodation_object, null);
            view.setId(View.generateViewId());
            accommParent.addView(view, 0,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

            // Change photo
            final View v = view;
            AsyncWrapper.wrap(() -> {
                try {
                    Bitmap image = listing.getPhotos().get(0);
                    getActivity().runOnUiThread(() -> {
                        ((ImageView) v.findViewById(R.id.listingThumbnail))
                                .setImageBitmap(image);
                    });
                } catch (Exception e) {
                    getActivity().runOnUiThread(() ->
                    {
                        ((ImageView) v.findViewById(R.id.listingThumbnail))
                                .setImageDrawable(getResources()
                                        .getDrawable(R.drawable.ic_buildings_filled));
                    });
                }
            });

            // Add the corresponding text
            ((TextView) view.findViewById(R.id.streetNameTxt))
                    .setText(listing.getAddressShort());
            ((TextView) view.findViewById(R.id.apartmentNameTxt)).setText(listing.getHouseNumber());

            // Set the needed constraints
            lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                lp.topToTop = previousView.getId();
            } else {
                lp.topToBottom = previousView.getId();
            }
            view.setLayoutParams(lp);

            // Remember the view
            myListingsObjects.add(new TenantAccommodationObject(view, listing));

            previousView = view;
            i++;
        }
    }

    private void addAllEditButtonsFunctionality() {
        for (TenantAccommodationObject a : myListingsObjects) {
            a.compactView.findViewById(R.id.settingsIcon).setOnClickListener(view1 -> {
                AlertDialog d = ((MainNavigationActivity) getActivity())
                        .editAccommodationDialog(a.compactView);
                getActivity().runOnUiThread(() -> setDialogInfo(d, a));
            });
        }
    }

    private void setDialogInfo(AlertDialog ad, TenantAccommodationObject a) {
        AccommodationInfo listing = a.accommodationInfo;
        AsyncWrapper.wrap(() -> {
            try {
                Bitmap image = listing.getPhotos().get(0);
                getActivity().runOnUiThread(() -> {
                    ((TextView) ad.findViewById(R.id.normalImageCountText))
                            //.setText(listing.getPhotos().size());
                            .setText("3");
                    ((ImageView) ad.findViewById(R.id.normalImage))
                            .setImageBitmap(image);
                });
            } catch (Exception e) {
                getActivity().runOnUiThread(() ->
                {
                    ((TextView) ad.findViewById(R.id.normalImageCountText))
                            .setText("0");
                    ((ImageView) ad.findViewById(R.id.normalImage))
                            .setImageDrawable(getResources()
                                    .getDrawable(R.drawable.ic_buildings_filled));
                });
            }
        });

        AsyncWrapper.wrap(() -> {
            try {
                Bitmap image = listing.getPhotoPanoramic();
                getActivity().runOnUiThread(() -> {
                    ((TextView) ad.findViewById(R.id.panoramaImageCountText))
                            .setText("1");
                    ((ImageView) ad.findViewById(R.id.panoramaImage))
                            .setImageBitmap(image);
                });
            } catch (Exception e) {
                getActivity().runOnUiThread(() -> {
                    ((TextView) ad.findViewById(R.id.panoramaImageCountText))
                            .setText("1");
                    ((ImageView) ad.findViewById(R.id.panoramaImage))
                            .setImageDrawable(getResources()
                                    .getDrawable(R.drawable.ic_buildings_filled));
                });
            }
        });

        ((TextView) ad.findViewById(R.id.addressTxt)).setText(listing.getAddressShort());
        ((TextView) ad.findViewById(R.id.apartmentNameText)).setText(listing.getHouseNumber());
        ((TextView) ad.findViewById(R.id.floorTxt)).setText(listing.getFloor());
        ((TextView) ad.findViewById(R.id.cityTxt)).setText(listing.getCity());
        ((TextView) ad.findViewById(R.id.postcodeTxt)).setText(listing.getPostcode());

        ((TextView) ad.findViewById(R.id.maxPriceTxt))
                .setText(listing.getCurrency() + listing.getPrice());
        ((TextView) ad.findViewById(R.id.minimumRentTxt)).setText(listing.getMinimumPeriod());
        ((TextView) ad.findViewById(R.id.startDateEditTxt)).setText(listing.getAvailableFrom());
        ((TextView) ad.findViewById(R.id.endDateEditTxt)).setText(listing.getAvailableUntil());
        ((TextView) ad.findViewById(R.id.surfaceAreaTxt)).setText(listing.getAreaString());
        ((TextView) ad.findViewById(R.id.descriptionTxt)).setText(listing.getDescription());

        ((CheckBox) ad.findViewById(R.id.furnishedCheckBox)).setChecked(listing.getFurnished());
        ((CheckBox) ad.findViewById(R.id.smokerCheckBox)).setChecked(listing.getSmokers());
        ((CheckBox) ad.findViewById(R.id.petsCheckBox)).setChecked(listing.getPets());

        ad.findViewById(R.id.saveBtn).setOnClickListener(view1 -> {
            updateAccommodationDetails(ad, listing);
            AsyncWrapper.wrap(() -> {
                try {
                    Database.updateAccommodation(listing.getAccommodationId(), transformAccommodation(listing));
                    Toast.makeText(getContext(), "Accommodation updated!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("ERR", e.getMessage());
                    Toast.makeText(getContext(), "Accommodation not updated!", Toast.LENGTH_SHORT).show();
                }
            });

            View view = a.compactView;
            ((TextView) view.findViewById(R.id.streetNameTxt))
                    .setText(listing.getAddressShort());
            ((TextView) view.findViewById(R.id.apartmentNameTxt)).setText(listing.getHouseNumber());

            ad.dismiss();
        });
    }

    private void updateAccommodationDetails(AlertDialog ad, AccommodationInfo listing) {
        listing.setAddress(((TextView) ad.findViewById(R.id.addressTxt)).getText().toString());

        listing.setCity(((TextView) ad.findViewById(R.id.cityTxt)).getText().toString());
        listing.setDescription1(((TextView) ad.findViewById(R.id.descriptionTxt)).getText().toString());
        listing.setAvailableUntil(((TextView) ad.findViewById(R.id.endDateEditTxt)).getText().toString());
        listing.setAvailableFrom(((TextView) ad.findViewById(R.id.startDateEditTxt)).getText().toString());
        listing.setFloor(((TextView) ad.findViewById(R.id.floorTxt)).getText().toString());
        listing.setFurnished(((CheckBox) ad.findViewById(R.id.furnishedCheckBox)).isChecked());
        listing.setHouseNumber(((TextView) ad.findViewById(R.id.apartmentNameText)).getText().toString());
        listing.setMinimumPeriod(((TextView) ad.findViewById(R.id.minimumRentTxt)).getText().toString());
        listing.setPets1(((CheckBox) ad.findViewById(R.id.petsCheckBox)).isChecked());
        listing.setPostcode(((TextView) ad.findViewById(R.id.postcodeTxt)).getText().toString());

        String s1 = ((TextView) ad.findViewById(R.id.maxPriceTxt)).getText().toString();
        if (s1.length() >= 1) s1 = s1.substring(1);
        listing.setPrice(Long.parseLong(s1));

        String s2 = ((TextView) ad.findViewById(R.id.surfaceAreaTxt)).getText().toString();
        listing.setArea_m2(Long.parseLong(s2));

        listing.setSmokers(((CheckBox) ad.findViewById(R.id.smokerCheckBox)).isChecked());

    }

    private Map<String, Object> transformAccommodation(AccommodationInfo listing) {
        Map<String, Object> transformedData = new HashMap<>();
        transformedData.put("address", listing.getAddress());
        transformedData.put("active", listing.getActive());
        transformedData.put("city", listing.getCity());
        transformedData.put("currency", listing.getCurrency());
        transformedData.put("description", listing.getDescription());
        transformedData.put("end_date", listing.getAvailableUntil());
        transformedData.put("floor", listing.getFloor());
        transformedData.put("furnished", listing.getFurnished());
        transformedData.put("house_number", listing.getHouseNumber());
        transformedData.put("minimum_rent_period", listing.getMinimumPeriod());
        transformedData.put("owner_username", listing.getOwnerUsername());
        transformedData.put("pets", listing.getPets());
        transformedData.put("post_code", listing.getPostcode());
        transformedData.put("price", listing.getPrice());
        transformedData.put("size_m2", listing.getArea());
        transformedData.put("smokers", listing.getSmokers());
        transformedData.put("start_date", listing.getAvailableFrom());
        return transformedData;
    }

    class TenantAccommodationObject {
        View compactView;
        AccommodationInfo accommodationInfo;

        TenantAccommodationObject(View compactAccomm, AccommodationInfo accommodationObj) {
            this.compactView = compactAccomm;
            this.accommodationInfo = accommodationObj;
        }
    }
}