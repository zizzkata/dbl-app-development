package com.example.dbl_app_dev;

import android.annotation.SuppressLint;
import android.media.Image;
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

/**
 * Discovery page fragment, if the user is in "Tenant" mode
 *
 * Use the {@link TenantDiscoverFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class TenantDiscoverFragment extends Fragment implements SwipeHandler {

    private Deque<AccommodationInfo> accommodationInfo = new LinkedList<>();
    private GestureDetector horizontalSwipeDetector;
    private GestureDetector verticalSwipeDetector;
    private AccommodationInfo currentAccommodationInfo = null;    // currently viewed accommodation
    private ImageView cardImageView;

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
        cardImageView = view.findViewById(R.id.accommodationPicture);

        this.horizontalSwipeDetector = new GestureDetector(getContext(), new CardSwipeListener(this, true, false));
        topCard.setOnTouchListener((v, event) -> {
            if (horizontalSwipeDetector.onTouchEvent(event)) {
                nextCard(nameTxt, cardImageView, descriptionTxt);
                return false;
            }
            return true;
        });

        Button likeBtn = view.findViewById(R.id.accommodationLikeBtn);
        Button dislikeBtn = view.findViewById(R.id.accommodationDislikeBtn);
        likeBtn.setOnClickListener(v -> {
            swipedRight();
            nextCard(nameTxt, cardImageView, descriptionTxt);
        });
        dislikeBtn.setOnClickListener(v -> {
            swipedLeft();
            nextCard(nameTxt, cardImageView, descriptionTxt);
        });

        // makes sure that the a card is not discarded if it is not rated
        if (currentAccommodationInfo == null) {
            nextCard(nameTxt, cardImageView, descriptionTxt);
        } else {
            displayCard(nameTxt, cardImageView, descriptionTxt);
        }
    }

    private void nextImage(ImageView cardImage) {
        currentAccommodationInfo.incrementPhotoIndex();
        cardImage.setImageBitmap(currentAccommodationInfo.getCurrentPhoto());
    }

    private void prevImage(ImageView cardImage) {
        currentAccommodationInfo.decrementPhotoIndex();
        cardImage.setImageBitmap(currentAccommodationInfo.getCurrentPhoto());
    }

    /**
     * Updates the information on currentAccommodationInfo and displays it
     */
    private void nextCard(TextView cardTitle, ImageView cardImage, TextView cardDescription) {
        if (accommodationInfo.size() > 0) {
            currentAccommodationInfo = this.accommodationInfo.remove();
            displayCard(cardTitle, cardImage, cardDescription);
        } else {
            cardDescription.setText("...");
            cardTitle.setText("No more swipes in your area");
        }
    }

    /**
     * Displays the information stored in currentAccommodationInfo
     */
    private void displayCard(TextView cardTitle, ImageView cardImage, TextView cardDescription) {
        cardTitle.setText(currentAccommodationInfo.getFormattedName());
        cardDescription.setText(currentAccommodationInfo.getDescription());
//        cardImage.setImageBitmap(currentAccommodationInfo.getPhotos().get(0));
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
    public void swipedRight() {
        if (accommodationInfo.size() > 0) {
            Log.d("extra_debug", "Positive Rating");
        }
    }

    /**
     * POST's the negative rating given to the viewed accommodation to the backend
     */
    @Override
    public void swipedLeft() {
        if (accommodationInfo.size() > 0) {
            Log.d("extra_debug", "Negative Rating");
        }
    }

    public void swipedUp() {
        Log.d("extra_debug", "Attempting to display previous card");
        // TODO:        prevImage(cardImageView);
    }

    public void swipedDown() {
        Log.d("extra_debug", "Attempting to display next card");
        // TODO:        nextImage(cardImageView);
    }
}