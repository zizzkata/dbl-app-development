package com.example.dbl_app_dev;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Discovery page for the user, if they are in "Landlord" mode
 *
 * Use the {@link LandlordDiscoverFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandlordDiscoverFragment extends Fragment implements RatingHandler {

    private Queue<TenantInfo> tenantInfo = new LinkedList<>();
    private GestureDetector gestureDetector;
    private TenantInfo currentTenantInfo;    // currently viewed tenant

    public LandlordDiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment landlordDiscoverFragment.
     */
    public static LandlordDiscoverFragment newInstance() {
        return new LandlordDiscoverFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landlord_discover, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert view != null;

        pullCardsInfo(10);
        ConstraintLayout topCard = view.findViewById(R.id.topCard);
        TextView nameTxt = view.findViewById(R.id.tenantName);
        TextView descriptionTxt = view.findViewById(R.id.tenantDescription);
        ImageView imageView = view.findViewById(R.id.tenantPicture);
        nextCard(nameTxt, imageView, descriptionTxt);

        this.gestureDetector = new GestureDetector(getContext(), new CardSwipeListener(this));
        topCard.setOnTouchListener((v, event) -> {
            if (gestureDetector.onTouchEvent(event)) {
                nextCard(nameTxt, imageView, descriptionTxt);
            }
            return true;
        });

        Button likeBtn = view.findViewById(R.id.tenantLikeBtn);
        Button dislikeBtn = view.findViewById(R.id.tenantDislikeBtn);
        Button neutralBtn = view.findViewById(R.id.tenantNeutralBtn);
        likeBtn.setOnClickListener(v -> {
            positiveRating();
            nextCard(nameTxt, imageView, descriptionTxt);
        });
        dislikeBtn.setOnClickListener(v -> {
            negativeRating();
            nextCard(nameTxt, imageView, descriptionTxt);
        });
        neutralBtn.setOnClickListener(v -> {
            neutralRating();
            nextCard(nameTxt, imageView, descriptionTxt);
        });
    }

    private void nextCard(TextView cardTitle, ImageView cardImage, TextView cardDescription) {
        if (tenantInfo.size() > 0) {
            currentTenantInfo = this.tenantInfo.remove();
            cardTitle.setText(currentTenantInfo.getName());
            cardDescription.setText(currentTenantInfo.getDescription());
//            cardImage.setImageBitmap(currentTenantInfo.getPhotos().get(0));
        } else {
            cardDescription.setText("...");
            cardTitle.setText("No more swipes in your area");
        }
    }

    /**
     * Pulls tenant data from server, adds it to tenantInfo
     *
     * @param batchSize number of cards to add to the tenantInfo queue
     */
    private void pullCardsInfo(int batchSize) {
        // TODO: remove placeholder code
        for (int i = 0; i < batchSize; i++) {
            tenantInfo.add(new TenantInfo(String.format("John Doe %d", i), String.format("Description %d", i), new ArrayList<>(), 21));
        }
    }

    /**
     * POST's the positive rating given to the viewed tenant to the backend
     */
    @Override
    public void positiveRating() {
        if (tenantInfo.size() > 0) {
            Log.i("extra_debug", "Positive Rating");
        }
    }

    /**
     * POST's the negative rating given to the viewed tenant to the backend
     */
    @Override
    public void negativeRating() {
        if (tenantInfo.size() > 0) {
            Log.i("extra_debug", "Negative Rating");
        }
    }

    /**
     * POST's the neutral rating given to the viewed tenant to the backend
     */
    @Override
    public void neutralRating() {
        if (tenantInfo.size() > 0) {
            Log.i("extra_debug", "Neutral Rating");
        }
    }
}