package com.example.dbl_app_dev;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
            ((MainNavigationActivity) getActivity()).createNewAccommodationDialog();
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
                                //addAllEditButtonsFunctionality();
                            }
                        }
                );
            } catch (Exception e) {
                Log.e("ERR", e.getMessage());
            }
        });

/*        // Add the accommodation settings button functionality
        final int childCount = accommParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = accommParent.getChildAt(i);
            v.findViewById(R.id.settingsIcon).setOnClickListener(view1 -> {
                ((MainNavigationActivity) getActivity()).editAccommodationDialog(v);
            });
        }*/
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

/*    private void addAllEditButtonsFunctionality() {
        for (TenantAccommodationObject a : myListingsObjects) {
            a.compactView.findViewById(R.id.actionIcon).setOnClickListener(view1 -> {
                AlertDialog d = ((MainNavigationActivity) getActivity())
                        .viewAccommodationDialog(a.compactView);
                getActivity().runOnUiThread(() -> setDialogInfo(d, a.accommodationInfo));
            });
        }
    }*/

    class TenantAccommodationObject {
        View compactView;
        AccommodationInfo accommodationInfo;

        TenantAccommodationObject(View compactAccomm, AccommodationInfo accommodationObj) {
            this.compactView = compactAccomm;
            this.accommodationInfo = accommodationObj;
        }
    }
}