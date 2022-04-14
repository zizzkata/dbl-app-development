package com.example.dbl_app_dev;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.dbl_app_dev.util.AsyncWrapper;
import com.example.dbl_app_dev.util.view_validation.validators.ImageValidator;
import com.example.dbl_app_dev.util.view_validation.validators.ViewValidator;

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

    abstract class OnClickListenerAdapter implements View.OnClickListener {
        private int request;

        public OnClickListenerAdapter(int request) {
            this.request = request;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, request);
        }
    }

    private AlertDialog currDialog;
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
            setCurrentDialog(d);
            addImageListeners(d);
            makeWarningsInvisible(d);

            d.findViewById(R.id.createButton).setOnClickListener(view2 -> {
                if (!isAccommodationValid(d)) {
                    return;
                }

                AccommodationInfo listing = getCurrentListing(d);
                AsyncWrapper.wrap(() -> {
                    try {
                        listing.pushAccommodation();

                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "New Accommodation Created",
                                        Toast.LENGTH_SHORT).show());

                    } catch (Exception e) {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Creation unsuccessful",
                                        Toast.LENGTH_SHORT).show());
                        return;
                    }

                    // Get the liked listings of the user
                    try {
                        // TODO properly push current listing to store or whatever
                        myListings.add(listing);
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
            });
        });


        // Get the parent view of the accommodation objects
        accommParent = getView().findViewById(R.id.scrollConstraintLayout);
        accommParent.removeAllViews();

        // Get the liked listings of the user
        AsyncWrapper.wrap(() -> {
            try {
                myListings = Database
                        .getActiveAccommodationsByOwner(Store.getCurrentUser().getUsername());
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

    private void makeWarningsInvisible(AlertDialog d) {
        d.findViewById(R.id.panoramaWarning).setVisibility(View.GONE);
        d.findViewById(R.id.imageWarning).setVisibility(View.GONE);
    }

    private void addImageListeners(AlertDialog ad) {
        ImageView panorama = ad.findViewById(R.id.panoramaImage);
        ImageView normal = ad.findViewById(R.id.normalImage);
        TextView addPanorama = ad.findViewById(R.id.addPanorama);
        TextView addImage = ad.findViewById(R.id.addNormal);

        final int panoramaRequest = 0;
        final int normalRequest = 1;

        panorama.setOnClickListener(new OnClickListenerAdapter(panoramaRequest) {
        });

        normal.setOnClickListener(new OnClickListenerAdapter(normalRequest) {
        });

        addPanorama.setOnClickListener(new OnClickListenerAdapter(panoramaRequest) {
        });

        addImage.setOnClickListener(new OnClickListenerAdapter(normalRequest) {
        });
    }

    private AccommodationInfo getCurrentListing(AlertDialog ad) {
        String username = "";
        try {
            username = Store.getCurrentUser().getUsername();
        } catch (Exception e) {
            // TODO
        }
        String address = ((TextView) ad.findViewById(R.id.addressTxt)).getText().toString();
        String houseNumber = ((TextView) ad.findViewById(R.id.apartmentNameText)).getText().toString();
        String floor = ((TextView) ad.findViewById(R.id.floorTxt)).getText().toString();
        String city = ((TextView) ad.findViewById(R.id.cityTxt)).getText().toString();
        String postcode = ((TextView) ad.findViewById(R.id.postcodeTxt)).getText().toString();
        String rentPeriod = ((TextView) ad.findViewById(R.id.minimumRentTxt)).getText().toString();
        String startDate = ((TextView) ad.findViewById(R.id.startDateEditTxt)).getText().toString();
        String endDate = ((TextView) ad.findViewById(R.id.endDateEditTxt)).getText().toString();
        String description = ((TextView) ad.findViewById(R.id.descriptionTxt)).getText().toString();
        boolean furnished = ((CheckBox) ad.findViewById(R.id.furnishedCheckBox)).isChecked();
        boolean smoker = ((CheckBox) ad.findViewById(R.id.smokerCheckBox)).isChecked();
        boolean pets = ((CheckBox) ad.findViewById(R.id.petsCheckBox)).isChecked();

        String s1 = ((TextView) ad.findViewById(R.id.maxPriceTxt)).getText().toString();
        String s2 = ((TextView) ad.findViewById(R.id.surfaceAreaTxt)).getText().toString();
        Long rentPrice = s1.equals("") ? 0 : Long.parseLong(s1);
        Long surfaceArea = s2.equals("") ? 0 : Long.parseLong(s2);

        ImageView panorama = ad.findViewById(R.id.panoramaImage);
        ImageView normal = ad.findViewById(R.id.normalImage);
        Bitmap panoramaBitmap = ((BitmapDrawable) panorama.getDrawable()).getBitmap();
        ArrayList<Bitmap> photos = new ArrayList<>();
        photos.add(((BitmapDrawable) normal.getDrawable()).getBitmap());

        AccommodationInfo listing = new AccommodationInfo(username, address, city,
                description, endDate, startDate, floor, furnished, pets, smoker,
                houseNumber, rentPeriod, postcode, rentPrice, surfaceArea, panoramaBitmap,
                photos);

        ad.dismiss();
        return listing;
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
                setCurrentDialog(d);
                addImageListeners(d);
                makeWarningsInvisible(d);
                removeListingFunctionality(d, a);
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
                .setText(listing.getPrice().toString());
        ((TextView) ad.findViewById(R.id.minimumRentTxt)).setText(listing.getMinimumPeriod());
        ((TextView) ad.findViewById(R.id.startDateEditTxt)).setText(listing.getAvailableFrom());
        ((TextView) ad.findViewById(R.id.endDateEditTxt)).setText(listing.getAvailableUntil());
        ((TextView) ad.findViewById(R.id.surfaceAreaTxt)).setText(listing.getAreaString());
        ((TextView) ad.findViewById(R.id.descriptionTxt)).setText(listing.getDescription());

        ((CheckBox) ad.findViewById(R.id.furnishedCheckBox)).setChecked(listing.getFurnished());
        ((CheckBox) ad.findViewById(R.id.smokerCheckBox)).setChecked(listing.getSmokers());
        ((CheckBox) ad.findViewById(R.id.petsCheckBox)).setChecked(listing.getPets());

        // TODO also update images
        ad.findViewById(R.id.saveBtn).setOnClickListener(view1 -> {
            if (!isAccommodationValid(ad)) {
                return;
            }

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
        listing.setPrice(s1.equals("") ? 0 : Long.parseLong(s1));

        String s2 = ((TextView) ad.findViewById(R.id.surfaceAreaTxt)).getText().toString();
        listing.setArea_m2(s2.equals("") ? 0 : Long.parseLong(s2));

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

        private void setListing(AccommodationInfo listing) {
            this.accommodationInfo = listing;
        }
    }

    private void setCurrentDialog(AlertDialog ad) {
        this.currDialog = ad;
    }

    private void removeListingFunctionality(AlertDialog ad, TenantAccommodationObject accommObj) {
        ad.findViewById(R.id.removeListingButton).setOnClickListener(view1 -> {
            AccommodationInfo listing = accommObj.accommodationInfo;
            listing.setActive(false);
            Map<String, Object> transformedData = new HashMap<>();
            transformedData.put("active", false);
            ad.dismiss();
            accommObj.compactView.setVisibility(View.GONE);

            AsyncWrapper.wrap(() -> {
                try {
                    Database.updateAccommodation(listing.getAccommodationId(), transformedData);
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Deletion successful!",
                                    Toast.LENGTH_SHORT).show());
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Deletion unsuccessful!",
                                    Toast.LENGTH_SHORT).show());
                }
            });
        });
    }

    private boolean isAccommodationValid(AlertDialog ad) {
        // list of all validator objects
        ArrayList<ViewValidator> validators = new ArrayList<>();

        ImageView panorama = ad.findViewById(R.id.panoramaImage);
        ImageView image = ad.findViewById(R.id.normalImage);
        TextView panoramaWarning = ad.findViewById(R.id.panoramaWarning);
        TextView imageWarning = ad.findViewById(R.id.imageWarning);

        validators.add(new ImageValidator(panorama, panoramaWarning));
        validators.add(new ImageValidator(image, imageWarning));

        // run validate on all validators
        boolean areValidatorsValid = true;
        for (ViewValidator validator : validators) {
            validator.validate();

            if (!validator.isValid()) {
                areValidatorsValid = false;
            }
        }
        // if all validators are valid, then return true, otherwise return false
        return areValidatorsValid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        Uri selectedImage = data.getData();
        ImageView imageView = (requestCode == 0)
                ? currDialog.findViewById(R.id.panoramaImage) : currDialog.findViewById(R.id.normalImage);
        imageView.setImageURI(selectedImage);
    }
}