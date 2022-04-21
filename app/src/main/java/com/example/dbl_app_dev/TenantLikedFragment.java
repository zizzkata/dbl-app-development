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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.dbl_app_dev.store.Store;
import com.example.dbl_app_dev.store.objects.AccommodationInfo;
import com.example.dbl_app_dev.store.objects.Rating;
import com.example.dbl_app_dev.store.objects.User;
import com.example.dbl_app_dev.util.AsyncWrapper;

import java.util.ArrayList;

public class TenantLikedFragment extends Fragment {
    enum rating {
        POSITIVE,
        NEUTRAL,
        NEGATIVE
    }

    ConstraintLayout positiveListingsParent;
    ConstraintLayout neutralListingsParent;
    ConstraintLayout negativeListingsParent;

    ArrayList<Rating> ratings = null;
    ArrayList<TenantLikedAccommodationObject> likedObjects = new ArrayList<>();

    public TenantLikedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tenant_liked, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the parent views of the different listing objects
        positiveListingsParent = getView()
                .findViewById(R.id.positiveListingsContainer);
        neutralListingsParent = getView()
                .findViewById(R.id.neutralListingsContainer);
        negativeListingsParent = getView()
                .findViewById(R.id.negativeListingsContainer);

        // Remove all previous listing objects
        positiveListingsParent.removeAllViews();
        neutralListingsParent.removeAllViews();
        negativeListingsParent.removeAllViews();

        // Add functionality for liked listings settings button
        Button settingsBtn = getView().findViewById(R.id.settingsButton);
        settingsBtn.setOnClickListener(view1 -> {
            AlertDialog dialog = ((MainNavigationActivity) getActivity())
                    .openLikedTenantSettingsDialog();
            setDialogCheckBoxes(dialog);
        });


        // Get the liked listings of the user
        AsyncWrapper.wrap(() -> {
            try {
                ArrayList<Rating> temp = Store.getRatingsTenant();
                ratings = new ArrayList<>();
                for (Rating rating : temp) {
                    if (rating.getRatingTenant() == 1) {
                        ratings.add(rating);
                    }
                }
                getActivity().runOnUiThread(() -> {
                            if (ratings != null) { // check that again, wwe may return one with size 0 if it fails
                                addLikedListings();
                                addAllInfoButtonsFunctionality();
                            }
                        }
                );
            } catch (Exception e) {
                Log.e("ERR", e.getMessage());
            }
        });

    }

    private void addLikedListings() {
        // Remembers the previously created view
        View previousView = null;
        ConstraintLayout view; // newly created view
        ConstraintLayout.LayoutParams lp; // LayoutParams for newly created view
        // Layout inflater for adding the new view
        LayoutInflater vi = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create the listings
        for (Rating rating : ratings) {

            if (rating == ratings.get(0)) previousView = getCurrentListingParent(
                    rating.getRatingLandlord().intValue());

            // Create the new view and add it to the parent
            view = (ConstraintLayout) vi
                    .inflate(getCurrentAccommodationLayoutId(
                            rating.getRatingLandlord().intValue()), null);
            view.setId(View.generateViewId());
            getCurrentListingParent(rating.getRatingTenant().intValue()).addView(view, 0,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

            final View v = view;
            // Download user if it doesn't exist
            AsyncWrapper.wrap(() -> {
                try {
                    AccommodationInfo accommodation = rating.getAccommodation();
                    getActivity().runOnUiThread(() -> {
                        // Add the corresponding text
                        ((TextView) v.findViewById(R.id.streetNameTxt))
                                .setText(accommodation.getAddressShort());
                        ((TextView) v.findViewById(R.id.apartmentNameTxt))
                                .setText(accommodation.getHouseNumber());
                        ((TextView) v.findViewById(R.id.priceTxt))
                                .setText(accommodation.getCurrency() + accommodation.getPrice());
                    });
                    Bitmap image = accommodation.getPhotos().get(0);
                    getActivity().runOnUiThread(() -> {
                        ((ImageView) v.findViewById(R.id.listingThumbnail))
                                .setImageBitmap(image);
                    });
                    // OLD EXCEPTION
//                } catch (Exception e) {
//                    getActivity().runOnUiThread(() ->
//                    {
//                        ((ImageView) v.findViewById(R.id.listingThumbnail))
//                                .setImageDrawable(getResources()
//                                        .getDrawable(R.drawable.ic_buildings_filled));
//                    });
//                }
                } catch (Exception e) {
                    // IGNORE
                    Log.e("getLikedListings", e.getMessage());
                    getActivity().runOnUiThread(() ->
                    {
                        ((ImageView) v.findViewById(R.id.listingThumbnail))
                                .setImageDrawable(getResources()
                                        .getDrawable(R.drawable.ic_buildings_filled));
                    });
                }
            });

            // Set the needed constraints
            lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (rating == ratings.get(0)) {
                lp.topToTop = previousView.getId();
            } else {
                lp.topToBottom = previousView.getId();
            }
            view.setLayoutParams(lp);

            // Remember the view
            likedObjects
                    .add(new TenantLikedAccommodationObject(view, rating
                            , rating.getRatingLandlord().intValue()));

            previousView = view;
        }
    }

    private ConstraintLayout getCurrentListingParent(int rating) {
        if (rating == 1) {
            return positiveListingsParent;
        } else if (rating == 0) {
            return neutralListingsParent;
        }

        return negativeListingsParent;
    }

    private int getCurrentAccommodationLayoutId(int rating) {
        if (rating == 1) {
            return R.layout.positive_accommodation_object;
        } else if (rating == 0) {
            return R.layout.neutral_accommodation_object;
        }

        return R.layout.negative_accommodation_object;
    }

    private void addAllInfoButtonsFunctionality() {
        for (TenantLikedAccommodationObject a : likedObjects) {
            if (a.rating == rating.POSITIVE || a.rating == rating.NEUTRAL) {
                a.compactView.findViewById(R.id.actionIcon).setOnClickListener(view1 -> {
                    AlertDialog d = ((MainNavigationActivity) getActivity())
                            .viewAccommodationDialog(a.compactView);
                    AsyncWrapper.wrap(() -> {
                        try {
                            AccommodationInfo acc = a.ratingObj.getAccommodation();
                            getActivity().runOnUiThread(() -> setDialogInfo(d, acc));
                        } catch (Exception e) {
                            // IGNORE
                        }
                    });
                });
            } else if (a.rating == rating.NEGATIVE) {
                // TODO remove liking from DB
                a.compactView.findViewById(R.id.actionIcon).setOnClickListener(view1 -> {
                    ((MainNavigationActivity) getActivity())
                            .removeAccommodationDialog(a.compactView);
                });
            }
        }
    }

    private void setDialogInfo(AlertDialog ad, AccommodationInfo listing) {
        AsyncWrapper.wrap(() -> {
            try {
                User owner = listing.getOwner();
                getActivity().runOnUiThread(() -> {
                    ((TextView) ad.findViewById(R.id.landlordNameTxt))
                            .setText(owner.getFirstName() + " " + owner.getLastName());
                    ((TextView) ad.findViewById(R.id.phoneNumberTxt))
                            .setText(owner.getPhoneNumber());
                });
            } catch (Exception e) {
                getActivity().runOnUiThread(() ->
                {
                    ((TextView) ad.findViewById(R.id.landlordNameTxt))
                            .setText("Georgi" + " " + "Georgiev");
                    ((TextView) ad.findViewById(R.id.phoneNumberTxt)).setText("0123456789");
                });
            }
        });

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
        ((TextView) ad.findViewById(R.id.floorNameText)).setText(listing.getFloor());
        ((TextView) ad.findViewById(R.id.cityTxt)).setText(listing.getCity());
        ((TextView) ad.findViewById(R.id.postcodeTxt)).setText(listing.getPostcode());

        ((TextView) ad.findViewById(R.id.priceTxt))
                .setText(listing.getCurrency() + listing.getPrice());
        ((TextView) ad.findViewById(R.id.minimumRentTxt)).setText(listing.getMinimumPeriod());
        ((TextView) ad.findViewById(R.id.startDateEditTxt)).setText(listing.getAvailableFrom());
        ((TextView) ad.findViewById(R.id.endDateEditTxt)).setText(listing.getAvailableUntil());
        ((TextView) ad.findViewById(R.id.surfaceAreaTxt)).setText(listing.getAreaString());
        ((TextView) ad.findViewById(R.id.descriptionTxt)).setText(listing.getDescription());

        ((CheckBox) ad.findViewById(R.id.furnishedCheckBox)).setChecked(listing.getFurnished());
        ((CheckBox) ad.findViewById(R.id.smokerCheckBox)).setChecked(listing.getSmokers());
        ((CheckBox) ad.findViewById(R.id.petsCheckBox)).setChecked(listing.getPets());
    }

    private void setDialogCheckBoxes(AlertDialog dialog) {
        ((androidx.appcompat.widget.SwitchCompat) dialog
                .findViewById(R.id.showPositiveListingsSwitch))
                .setChecked(positiveListingsParent.getVisibility() == View.VISIBLE);
        ((androidx.appcompat.widget.SwitchCompat) dialog
                .findViewById(R.id.showNeutralListingsSwitch))
                .setChecked(neutralListingsParent.getVisibility() == View.VISIBLE);
        ((androidx.appcompat.widget.SwitchCompat) dialog
                .findViewById(R.id.showNegativeListingsSwitch12))
                .setChecked(negativeListingsParent.getVisibility() == View.VISIBLE);
    }

    class TenantLikedAccommodationObject {
        View compactView;
        Rating ratingObj;
        rating rating;

        TenantLikedAccommodationObject(View compactAccomm,
                                       Rating ratingObj, int _r) {
            this.compactView = compactAccomm;
            this.ratingObj = ratingObj;

            rating r = rating.NEGATIVE;
            if (_r == 1) r = rating.POSITIVE;
            if (_r == 0) r = rating.NEUTRAL;
            this.rating = r;
        }
    }
}