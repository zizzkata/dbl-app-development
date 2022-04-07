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
import com.example.dbl_app_dev.util.AsyncWrapper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TenantLikedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TenantLikedFragment extends Fragment {
    enum rating {
        POSITIVE,
        NEUTRAL,
        NEGATIVE
    }

    ConstraintLayout positiveListingsParent;
    ConstraintLayout neutralListingsParent;
    ConstraintLayout negativeListingsParent;

    ArrayList<AccommodationInfo> likedListings = null;
    ArrayList<TenantLikedAccommodationObject> likedObjects = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TenantLikedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment firstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TenantLikedFragment newInstance(String param1, String param2) {
        TenantLikedFragment fragment = new TenantLikedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        //positiveListingsParent.removeAllViews();
        neutralListingsParent.removeAllViews();
        //negativeListingsParent.removeAllViews();

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
                likedListings = Store.getCurrentUserLikedAccommodations();
                getActivity().runOnUiThread(() -> {
                            if (likedListings != null) {
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
        int i = 0;
        for (AccommodationInfo listing : likedListings) {
            if (i == 0) previousView = getCurrentListingParent(likedListings.get(0).getRating());

            // Create the new view and add it to the parent
            view = (ConstraintLayout) vi
                    .inflate(getCurrentAccommodationLayoutId(listing.getRating()), null);
            view.setId(View.generateViewId());
            getCurrentListingParent(listing.getRating()).addView(view, 0,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

            // Add the corresponding text
            ((TextView) view.findViewById(R.id.streetNameTxt))
                    .setText(listing.getAddressShort());
            ((TextView) view.findViewById(R.id.apartmentNameTxt)).setText(listing.getHouseNumber());
            ((TextView) view.findViewById(R.id.priceTxt))
                    .setText(listing.getCurrency() + listing.getPrice());

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
            likedObjects
                    .add(new TenantLikedAccommodationObject(view, listing, listing.getRating()));

            previousView = view;
            i++;
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
                    getActivity().runOnUiThread(() -> setDialogInfo(d, a.accommodationInfo));
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
        // TODO
        ((TextView) ad.findViewById(R.id.landlordNameTxt)).setText("Georgi" + " " + "Georgiev");
        ((TextView) ad.findViewById(R.id.landlordEmailTxt)).setText("gosho@gmail.com");
        ((TextView) ad.findViewById(R.id.phoneNumberTxt)).setText("0123456789");

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
        AccommodationInfo accommodationInfo;
        rating rating;

        TenantLikedAccommodationObject(View compactAccomm,
                                       AccommodationInfo accommodationObj, int _r) {
            this.compactView = compactAccomm;
            this.accommodationInfo = accommodationObj;

            rating r = rating.NEGATIVE;
            if (_r == 1) r = rating.POSITIVE;
            if (_r == 0) r = rating.NEUTRAL;
            this.rating = r;
        }
    }
}