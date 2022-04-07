package com.example.dbl_app_dev;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
    ConstraintLayout noSwipesContainer;
    ConstraintLayout contentContainer;
    ImageViewPagerAdapter verticalViewPagerAdapter;

    public TenantDiscoverFragment() {
        /* Required empty public constructor */
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
        ArrayList<TextView> cardTextViews = new ArrayList<>();
        cardTextViews.add(view.findViewById(R.id.addressTxt));
        cardTextViews.add(view.findViewById(R.id.floorTxt));
        cardTextViews.add(view.findViewById(R.id.postcodeTxt));
        cardTextViews.add(view.findViewById(R.id.priceTxt));
        cardTextViews.add(view.findViewById(R.id.accommTypeTxt));
        cardTextViews.add(view.findViewById(R.id.utilitiesTxt));
        cardTextViews.add(view.findViewById(R.id.areaTxt));
        cardTextViews.add(view.findViewById(R.id.furnishedTxt));
        cardTextViews.add(view.findViewById(R.id.petsTxt));
        cardTextViews.add(view.findViewById(R.id.smokersTxt));
        cardTextViews.add(view.findViewById(R.id.minimumPeriodTxt));
        cardTextViews.add(view.findViewById(R.id.availableTxt));
        cardTextViews.add(view.findViewById(R.id.untilTxt));
        cardTextViews.add(view.findViewById(R.id.descriptionTxt));

        imageGalleryViewPager = view.findViewById(R.id.accommodationImageScroller);

        noSwipesContainer = view.findViewById(R.id.noSwipesContainer);
        contentContainer = view.findViewById(R.id.contentContainer);
        noSwipesContainer.setVisibility(View.INVISIBLE);
        contentContainer.setVisibility(View.VISIBLE);

        this.horizontalSwipeDetector = new GestureDetector(getContext(), new CardSwipeListener(this, true, false));
        topCard.setOnTouchListener((v, event) -> {
            if (horizontalSwipeDetector.onTouchEvent(event)) {
                nextCard(cardTextViews, imageGalleryViewPager);
                return false;
            }
            return true;
        });

        Button likeBtn = view.findViewById(R.id.positiveButton);
        Button dislikeBtn = view.findViewById(R.id.negativeButton);
        Button arBtn = view.findViewById(R.id.arButton);

        likeBtn.setOnClickListener(v -> {
            swipedRight();
            nextCard(cardTextViews, imageGalleryViewPager);
        });
        dislikeBtn.setOnClickListener(v -> {
            swipedLeft();
            nextCard(cardTextViews, imageGalleryViewPager);
        });
        arBtn.setOnClickListener(v -> {
            imageGalleryViewPager.setAdapter(verticalViewPagerAdapter);
        });

        Button filtersBtn = view.findViewById(R.id.filtersButton);
        filtersBtn.setOnClickListener(v -> ((MainNavigationActivity) requireActivity()).openFilterDialog());

        // makes sure that the a card is not discarded if it is not rated
        if (currentAccommodationInfo == null) {
            nextCard(cardTextViews, imageGalleryViewPager);
        } else {
            displayCard(cardTextViews, imageGalleryViewPager);
        }
    }

    /**
     * Updates the information on currentAccommodationInfo and displays it
     */
    private void nextCard(ArrayList<TextView> cardTextViews, VerticalViewPager imageGalleryViewPager) {
        if (dataModels.size() > 0) {
            currentAccommodationInfo = this.dataModels.remove();
            displayCard(cardTextViews, imageGalleryViewPager);
        } else {
            // no more swipes left
            currentAccommodationInfo = null;
            noSwipesContainer.setVisibility(View.VISIBLE);
            contentContainer.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Displays the information stored in currentAccommodationInfo
     */
    private void displayCard(ArrayList<TextView> cardTextViews, VerticalViewPager imageGalleryViewPager) {
        ArrayList<String> cardStrings = currentAccommodationInfo.getCardFormattedText();
        assert (cardStrings.size() == cardTextViews.size()) : "Incorrect size of accommodation info strings";
        for (int i = 0; i < cardStrings.size(); i++) {
            cardTextViews.get(i).setText(cardStrings.get(i));
        }
        verticalViewPagerAdapter = new ImageViewPagerAdapter(getChildFragmentManager(), currentAccommodationInfo.getPhotos(), currentAccommodationInfo.getPhotoPanoramic());
        imageGalleryViewPager.setAdapter(verticalViewPagerAdapter);
    }

    /**
     * Pulls accommodation data from server, adds it to accommodationInfo
     *
     * @param batchSize number of cards to add to the accommodationInfo queue
     */
    private void pullCardsInfo(int batchSize) {
        // TODO: remove placeholder code, pull data from the server
        // START PLACEHOLDER CODE
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.default_accommodation_picture);
        Bitmap panoramicImage = null;
        InputStream inputStream;
        AssetManager assetManager = requireContext().getAssets();
        try {
            inputStream = assetManager.open("yosemite.jpg");
            System.out.println(inputStream.toString());
            panoramicImage = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < batchSize; i++) {
            String[] sample = new String[14];
            Arrays.fill(sample, "text" + i);
            ArrayList<String> sampleArrList = new ArrayList<>(Arrays.asList(sample));

            ArrayList<Bitmap> images = new ArrayList<>();
            images.add(image);
            images.add(image);

            dataModels.add(new AccommodationInfo(sampleArrList, images, panoramicImage));
        }
        // END PLACEHOLDER CODE
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
