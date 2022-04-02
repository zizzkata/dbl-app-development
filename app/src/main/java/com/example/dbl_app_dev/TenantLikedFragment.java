package com.example.dbl_app_dev;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TenantLikedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TenantLikedFragment extends Fragment {

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

        // Add functionality for liked listings settings button
        Button settingsBtn = getView().findViewById(R.id.settingsButton);
        settingsBtn.setOnClickListener(view1 -> {
            ((MainNavigationActivity) getActivity()).openLikedTenantSettingsDialog();
        });

        // Get the parent views of the different listing objects
        ConstraintLayout positiveListingsParent = getView()
                .findViewById(R.id.positiveListingsContainer);
        ConstraintLayout neutralListingsParent = getView()
                .findViewById(R.id.neutralListingsContainer);
        ConstraintLayout negativeListingsParent = getView()
                .findViewById(R.id.negativeListingsContainer);

        // Add the positive listing "info" button functionality
        for (int i = 0; i < positiveListingsParent.getChildCount(); i++) {
            View v = positiveListingsParent.getChildAt(i);
            v.findViewById(R.id.actionIcon).setOnClickListener(view1 -> {
                ((MainNavigationActivity) getActivity()).viewAccommodationDialog(v);
            });
        }

        // Add the neutral listing "info" button functionality
        for (int i = 0; i < neutralListingsParent.getChildCount(); i++) {
            View v = neutralListingsParent.getChildAt(i);
            v.findViewById(R.id.actionIcon).setOnClickListener(view1 -> {
                ((MainNavigationActivity) getActivity()).viewAccommodationDialog(v);
            });
        }

        // Add the negative listing "remove" button functionality
        for (int i = 0; i < negativeListingsParent.getChildCount(); i++) {
            View v = negativeListingsParent.getChildAt(i);
            v.findViewById(R.id.actionIcon).setOnClickListener(view1 -> {
                ((MainNavigationActivity) getActivity()).removeAccommodationDialog(v);
            });
        }
    }
}