package com.example.dbl_app_dev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LandlordAccommodationManagementFragment#newInstance} factory
 * method to
 * create an instance of this fragment.
 */
public class LandlordAccommodationManagementFragment extends Fragment {

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
        ConstraintLayout accommParent = getView()
                .findViewById(R.id.scrollConstraintLayout);

        // Add the accommodation settings button functionality
        final int childCount = accommParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = accommParent.getChildAt(i);
            v.findViewById(R.id.settingsIcon).setOnClickListener(view1 -> {
                ((MainNavigationActivity) getActivity()).editAccommodationDialog(v);
            });
        }
    }
}