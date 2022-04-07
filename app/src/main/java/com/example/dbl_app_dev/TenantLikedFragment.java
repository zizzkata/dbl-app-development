package com.example.dbl_app_dev;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
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

import com.example.dbl_app_dev.store.objects.AccommodationInfo;

import java.util.ArrayList;
import java.util.LinkedList;

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

    ArrayList<TenantLikedAccommodationObject> likedObjects = new ArrayList<>();

    LinkedList<AccommodationInfo> likedListings = null;

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
        positiveListingsParent.removeAllViews();
        neutralListingsParent.removeAllViews();
        negativeListingsParent.removeAllViews();

        // Add functionality for liked listings settings button
        Button settingsBtn = getView().findViewById(R.id.settingsButton);
        settingsBtn.setOnClickListener(view1 -> {
            ((MainNavigationActivity) getActivity()).openLikedTenantSettingsDialog();
        });

        // Get the liked listings of the user
/*        try {
            likedListings = Store.getLikedListings();
        } catch (Exception e) {
            Log.e("OPS", "OPS");
            e.printStackTrace();
            return;
        }*/
        if (likedListings != null) {
            addLikedListings(positiveListingsParent, R.layout.positive_accommodation_object);
            addAllInfoButtonsFunctionality();
        }

    }

    private void addLikedListings(ConstraintLayout currentListingParent,
                                  int currentAccommodationLayoutId) {
        View previousView = currentListingParent; // to remember the previously created view
        ConstraintLayout view; // newly created view
        ConstraintLayout.LayoutParams lp; // LayoutParams for newly created view
        // Layout inflater for adding the new view
        LayoutInflater vi = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create the listings
        int i = 0;
        for (AccommodationInfo listing : likedListings) {
            // Create the new view and add it to the parent
            view = (ConstraintLayout) vi.inflate(currentAccommodationLayoutId, null);
            view.setId(View.generateViewId());
            currentListingParent.addView(view, 0,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

            // Add the corresponding text
            ((TextView) view.findViewById(R.id.streetNameTxt)).setText(listing.getAddress());
            ((TextView) view.findViewById(R.id.apartmentNameTxt)).setText(listing.getHouseNumber());
            ((TextView) view.findViewById(R.id.priceTxt))
                    .setText(listing.getCurrency() + listing.getPrice());

            // Set the needed constraints
            lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                lp.topToTop = previousView.getId();
            } else {
                lp.topToBottom = previousView.getId();
            }
            view.setLayoutParams(lp);

            // Remember the view
            likedObjects.add(new TenantLikedAccommodationObject(view, listing, rating.NEUTRAL));

            previousView = view;
            i++;
        }
    }

    private void addAllInfoButtonsFunctionality() {
        for (TenantLikedAccommodationObject a: likedObjects) {
            if (a.rating == rating.POSITIVE || a.rating == rating.NEUTRAL) {
                a.compactView.findViewById(R.id.actionIcon).setOnClickListener(view1 -> {
                    AlertDialog d = ((MainNavigationActivity) getActivity()).viewAccommodationDialog(a.compactView);
                    getActivity().runOnUiThread(() -> setDialogInfo(d, a.accommodationInfo));
                });
            } else if (a.rating == rating.NEGATIVE) {
                a.compactView.findViewById(R.id.actionIcon).setOnClickListener(view1 -> {
                    ((MainNavigationActivity) getActivity()).removeAccommodationDialog(a.compactView);
                });
            }

        }
/*        // Add the positive listing "info" button functionality
        for (int i = 0; i < positiveListingsParent.getChildCount(); i++) {
            View v = positiveListingsParent.getChildAt(i);
            v.findViewById(R.id.actionIcon).setOnClickListener(view1 -> {
                AlertDialog d = ((MainNavigationActivity) getActivity()).viewAccommodationDialog(v);
                //AccommodationInfo accommodationInfo = new AccommodationInfo();
                //getActivity().runOnUiThread(() -> setDialogInfo(d, accommodationInfo));
            });
        }

        // Add the neutral listing "info" button functionality
        for (int i = 0; i < neutralListingsParent.getChildCount(); i++) {
            View v = neutralListingsParent.getChildAt(i);
            v.findViewById(R.id.actionIcon).setOnClickListener(view1 -> {
                AlertDialog d = ((MainNavigationActivity) getActivity()).viewAccommodationDialog(v);
                //AccommodationInfo accommodationInfo = new AccommodationInfo();
                //getActivity().runOnUiThread(() -> setDialogInfo(d, accommodationInfo));
            });
        }

        // Add the negative listing "remove" button functionality
        for (int i = 0; i < negativeListingsParent.getChildCount(); i++) {
            View v = negativeListingsParent.getChildAt(i);
            v.findViewById(R.id.actionIcon).setOnClickListener(view1 -> {
                ((MainNavigationActivity) getActivity()).removeAccommodationDialog(v);
            });
        }*/
    }

    private void setDialogInfo(AlertDialog ad, AccommodationInfo listing) {
        // TODO
        ((TextView) ad.findViewById(R.id.landlordNameTxt)).setText("First" + "Name");
        ((TextView) ad.findViewById(R.id.landlordEmailTxt)).setText("myEmail");
        ((TextView) ad.findViewById(R.id.phoneNumberTxt)).setText("0000000000");

        // TODO
        ((ImageView) ad.findViewById(R.id.panoramaImage))
                .setImageDrawable(getResources().getDrawable(R.drawable.ic_buildings_filled));
        ((ImageView) ad.findViewById(R.id.normalImage))
                .setImageDrawable(getResources().getDrawable(R.drawable.ic_buildings_filled));

        ((TextView) ad.findViewById(R.id.addressTxt)).setText(listing.getAddress());
        ((TextView) ad.findViewById(R.id.apartmentNameText)).setText(listing.getHouseNumber());
        ((TextView) ad.findViewById(R.id.floorNameText)).setText(listing.getFloor());
        ((TextView) ad.findViewById(R.id.cityTxt)).setText(listing.getCity());
        ((TextView) ad.findViewById(R.id.postcodeTxt)).setText(listing.getPostcode());

        ((TextView) ad.findViewById(R.id.priceTxt)).setText(listing.getCurrency() + listing.getPrice());
        ((TextView) ad.findViewById(R.id.minimumRentTxt)).setText(listing.getMinimumPeriod());
        ((TextView) ad.findViewById(R.id.startDateEditTxt)).setText(listing.getAvailableFrom());
        ((TextView) ad.findViewById(R.id.endDateEditTxt)).setText(listing.getAvailableUntil());
        ((TextView) ad.findViewById(R.id.surfaceAreaTxt)).setText(listing.getAreaString());
        ((TextView) ad.findViewById(R.id.descriptionTxt)).setText(listing.getDescription());

        ((CheckBox) ad.findViewById(R.id.furnishedCheckBox)).setChecked(listing.getFurnished());
        ((CheckBox) ad.findViewById(R.id.smokerCheckBox)).setChecked(listing.getSmokers());
        ((CheckBox) ad.findViewById(R.id.petsCheckBox)).setChecked(listing.getPets());
    }

    class TenantLikedAccommodationObject {
        View compactView;
        AccommodationInfo accommodationInfo;
        rating rating;

        TenantLikedAccommodationObject(View compactAccomm,
                                       AccommodationInfo accommodationObj, rating rating) {
            this.compactView = compactAccomm;
            this.accommodationInfo = accommodationObj;
            this.rating = rating;
        }
    }
}