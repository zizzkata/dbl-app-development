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
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Discovery page fragment, if the user is in "Tenant" mode
 * <p>
 * Use the {@link TenantDiscoverFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class TenantDiscoverFragment extends Fragment implements RatingHandler {

    private Deque<AccommodationInfo> accommodationInfo = new LinkedList<>();
    private GestureDetector gestureDetector;
    private AccommodationInfo currentAccommodationInfo = null;    // currently viewed accommodation

    public TenantDiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment tenantDiscoverFragment.
     */
    public static TenantDiscoverFragment newInstance() {
        return new TenantDiscoverFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tenant_discover, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pullCardsInfo(10);
        ConstraintLayout topCard = view.findViewById(R.id.topCard);
        TextView nameTxt = view.findViewById(R.id.accommodationName);
        TextView descriptionTxt = view.findViewById(R.id.accommodationDescription);
        ImageView imageView = view.findViewById(R.id.accommodationPicture);

        // makes sure that the a card is not discarded if it is not rated
        if (currentAccommodationInfo == null) {
            nextCard(nameTxt, imageView, descriptionTxt);
        } else {
            accommodationInfo.addFirst(currentAccommodationInfo);
            nextCard(nameTxt, imageView, descriptionTxt);
        }

        this.gestureDetector = new GestureDetector(getContext(), new CardSwipeListener(this));
        topCard.setOnTouchListener((v, event) -> {
            if (gestureDetector.onTouchEvent(event)) {
                nextCard(nameTxt, imageView, descriptionTxt);
            }
            return true;
        });

        Button likeBtn = view.findViewById(R.id.accommodationLikeBtn);
        Button dislikeBtn = view.findViewById(R.id.accommodationDislikeBtn);
        likeBtn.setOnClickListener(v -> {
            positiveRating();
            nextCard(nameTxt, imageView, descriptionTxt);
        });
        dislikeBtn.setOnClickListener(v -> {
            negativeRating();
            nextCard(nameTxt, imageView, descriptionTxt);
        });
    }

    private void nextCard(TextView cardTitle, ImageView cardImage, TextView cardDescription) {
        if (accommodationInfo.size() > 0) {
            currentAccommodationInfo = this.accommodationInfo.remove();
            cardTitle.setText(currentAccommodationInfo.getFormattedName());
            cardDescription.setText(currentAccommodationInfo.getDescription());
//            cardImage.setImageBitmap(currentAccommodationInfo.getPhotos().get(0));
        } else {
            cardDescription.setText("...");
            cardTitle.setText("No more swipes in your area");
        }
    }

    /**
     * Pulls accommodation data from server, adds it to accommodationInfo
     *
     * @param batchSize number of cards to add to the accommodationInfo queue
     */
    private void pullCardsInfo(int batchSize) {
        // TODO: remove placeholder code
        for (int i = 0; i < batchSize; i++) {
            accommodationInfo.add(new AccommodationInfo(String.format("Building %d", i), String.format("Description %d", i), new ArrayList<>(), null, 720));
        }
    }

    /**
     * POST's the positive rating given to the viewed accommodation to the backend
     */
    @Override
    public void positiveRating() {
        if (accommodationInfo.size() > 0) {
            Log.i("extra_debug", "Positive Rating");
        }
    }

    /**
     * POST's the negative rating given to the viewed accommodation to the backend
     */
    @Override
    public void negativeRating() {
        if (accommodationInfo.size() > 0) {
            Log.i("extra_debug", "Negative Rating");
        }
    }

    /**
     * Cannot neutrally rate an accommodation
     */
    @Override
    public void neutralRating() {
    }
}