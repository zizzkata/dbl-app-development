package com.example.dbl_app_dev;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Discovery page fragment, if the user is in "Tenant" mode
 * <p>
 * Use the {@link TenantDiscoverFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class TenantDiscoverFragment extends Fragment implements SwipeHandler {
    private LinkedList<AccommodationInfo> dataModels;
    private GestureDetector horizontalSwipeDetector;
    private AccommodationInfo currentAccommodationInfo = null; // currently viewed accommodation
    VerticalViewPager imageGalleryViewPager;

    public TenantDiscoverFragment() {
        /* Required empty public constructor */ }

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
        // Get information of accommodation cards
        dataModels = new LinkedList<>();
        pullCardsInfo(10);
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

        ConstraintLayout topCard = view.findViewById(R.id.topCard);
        TextView nameTxt = view.findViewById(R.id.accommodationName);
        TextView descriptionTxt = view.findViewById(R.id.accommodationDescription);

        imageGalleryViewPager = view.findViewById(R.id.accommodationImageScroller);
        imageGalleryViewPager = view.findViewById(R.id.accommodationImageScroller);

        this.horizontalSwipeDetector = new GestureDetector(getContext(), new CardSwipeListener(this, true, false));
        topCard.setOnTouchListener((v, event) -> {
            if (horizontalSwipeDetector.onTouchEvent(event)) {
                nextCard(nameTxt, imageGalleryViewPager, descriptionTxt);
                return false;
            }
            return true;
        });

        Button likeBtn = view.findViewById(R.id.accommodationLikeBtn);
        Button dislikeBtn = view.findViewById(R.id.accommodationDislikeBtn);

        likeBtn.setOnClickListener(v -> {
            swipedRight();
            nextCard(nameTxt, imageGalleryViewPager, descriptionTxt);
        });
        dislikeBtn.setOnClickListener(v -> {
            swipedLeft();
            nextCard(nameTxt, imageGalleryViewPager, descriptionTxt);
        });

        imageGalleryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // mandatory override
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("API123", "onPageSelected " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // mandatory override
            }
        });

        // makes sure that the a card is not discarded if it is not rated
        if (currentAccommodationInfo == null) {
            nextCard(nameTxt, imageGalleryViewPager, descriptionTxt);
        } else {
            displayCard(nameTxt, imageGalleryViewPager, descriptionTxt);
        }

    }

    /**
     * Updates the information on currentAccommodationInfo and displays it
     */
    private void nextCard(TextView cardTitle, VerticalViewPager imageGalleryViewPager, TextView cardDescription) {
        if (dataModels.size() > 0) {
            currentAccommodationInfo = this.dataModels.remove();
            displayCard(cardTitle, imageGalleryViewPager, cardDescription);
        } else {
            cardDescription.setText("...");
            cardTitle.setText("No more swipes in your area");
        }
    }

    /**
     * Displays the information stored in currentAccommodationInfo
     */
    private void displayCard(TextView cardTitle, VerticalViewPager imageGalleryViewPager, TextView cardDescription) {
        cardTitle.setText(currentAccommodationInfo.getFormattedName());
        cardDescription.setText(currentAccommodationInfo.getDescription());
        imageGalleryViewPager
                .setAdapter(new ImageViewPagerAdapter(getChildFragmentManager(), currentAccommodationInfo.getPhotos()));
    }

    /**
     * Pulls accommodation data from server, adds it to accommodationInfo
     *
     * @param batchSize number of cards to add to the accommodationInfo queue
     */
    private void pullCardsInfo(int batchSize) {
        // TODO: remove placeholder code, pull data from the server
        Bitmap image = null;
        image = BitmapFactory.decodeResource(getResources(), R.drawable.default_accommodation_picture);
        for (int i = 0; i < batchSize; i++) {
            ArrayList<Bitmap> images = new ArrayList<>();
            images.add(image);
            images.add(image);
            dataModels.add(new AccommodationInfo(String.format("Building %d", i), String.format("Description %d", i),
                    images, null, 720));
        }
    }

    /**
     * POST's the positive rating given to the viewed accommodation to the backend
     */
    @Override
    public void swipedRight() {
        if (dataModels.size() > 0) {
            Log.d("extra_debug", "Positive Rating");
        }
    }

    /**
     * POST's the negative rating given to the viewed accommodation to the backend
     */
    @Override
    public void swipedLeft() {
        if (dataModels.size() > 0) {
            Log.d("extra_debug", "Negative Rating");
        }
    }
}
