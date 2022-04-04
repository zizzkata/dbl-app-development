package com.example.dbl_app_dev;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Discovery page fragment, if the user is in "Landlord" mode
 * <p>
 * Use the {@link LandlordDiscoverFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandlordDiscoverFragment extends Fragment implements SwipeHandler {

    private LinkedList<TenantInfo> tenantInfo;
    private GestureDetector swipeListener;
    private TenantInfo currentTenantInfo = null; // currently viewed tenant
    ConstraintLayout noSwipesContainer;
    ConstraintLayout contentContainer;

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
        tenantInfo = new LinkedList<>();
        pullCardsInfo(10);
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

        ConstraintLayout topCard = view.findViewById(R.id.topCard);

        ImageView imageView = view.findViewById(R.id.tenantPicture);

        ArrayList<TextView> cardTextViews = new ArrayList<>();
        cardTextViews.add(view.findViewById(R.id.nameTxt));
        cardTextViews.add(view.findViewById(R.id.emailTxt));
        cardTextViews.add(view.findViewById(R.id.phoneNumberTxt));
        cardTextViews.add(view.findViewById(R.id.petsTxt));
        cardTextViews.add(view.findViewById(R.id.smokerTxt));
        cardTextViews.add(view.findViewById(R.id.descriptionTxt));

        noSwipesContainer = view.findViewById(R.id.noSwipesContainer);
        contentContainer = view.findViewById(R.id.contentContainer);
        noSwipesContainer.setVisibility(View.INVISIBLE);
        contentContainer.setVisibility(View.VISIBLE);

        // makes sure that the a card is not discarded if it is not rated
        if (currentTenantInfo == null) {
            nextCard(cardTextViews, imageView);
        } else {
            displayCard(cardTextViews, imageView);
        }

        this.swipeListener = new GestureDetector(getContext(), new CardSwipeListener(this, true, true));
        topCard.setOnTouchListener((v, event) -> {
            if (swipeListener.onTouchEvent(event)) {
                nextCard(cardTextViews, imageView);
            }
            return true;
        });

        Button likeBtn = view.findViewById(R.id.positiveButton);
        Button dislikeBtn = view.findViewById(R.id.negativeButton);
        Button neutralBtn = view.findViewById(R.id.neutralButton);
        likeBtn.setOnClickListener(v -> {
            swipedRight();
            nextCard(cardTextViews, imageView);
        });
        dislikeBtn.setOnClickListener(v -> {
            swipedLeft();
            nextCard(cardTextViews, imageView);
        });
        neutralBtn.setOnClickListener(v -> {
            swipedDown();
            nextCard(cardTextViews, imageView);
        });
    }

    /**
     * Displays the information stored in currentAccommodationInfo
     */
    private void displayCard(ArrayList<TextView> cardTextViews, ImageView cardImage) {
        ArrayList<String> cardStrings = currentTenantInfo.getFormattedText();

        assert (cardStrings.size() == cardTextViews.size()) : "Incorrect size of tenant info strings";
        for (int i = 0; i < cardStrings.size(); i++) {
            cardTextViews.get(i).setText(cardStrings.get(i));
        }
        cardImage.setImageBitmap(currentTenantInfo.getPhoto());
    }

    private void nextCard(ArrayList<TextView> cardTitle, ImageView cardImage) {
        if (tenantInfo.size() > 0) {
            currentTenantInfo = this.tenantInfo.remove();
            displayCard(cardTitle, cardImage);
        } else {
            // no more swipes left
            currentTenantInfo = null;
            noSwipesContainer.setVisibility(View.VISIBLE);
            contentContainer.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Pulls tenant data from server, adds it to tenantInfo
     *
     * @param batchSize number of cards to add to the tenantInfo queue
     */
    private void pullCardsInfo(int batchSize) {
        // TODO: remove placeholder code, get data from server
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.default_tenant_picture);
        for (int i = 0; i < batchSize; i++) {
            String[] sample = new String[6];
            Arrays.fill(sample, "text" + i);
            ArrayList<String> sampleArrList = new ArrayList<>(Arrays.asList(sample));

            tenantInfo.add(
                    new TenantInfo(sampleArrList, image));
        }
    }

    /**
     * POST's the positive rating given to the viewed tenant to the backend
     */
    @Override
    public void swipedRight() {
        if (tenantInfo.size() > 0) {
            Log.i("extra_debug", "Positive Rating");
        }
    }

    /**
     * POST's the negative rating given to the viewed tenant to the backend
     */
    @Override
    public void swipedLeft() {
        if (tenantInfo.size() > 0) {
            Log.i("extra_debug", "Negative Rating");
        }
    }

    /**
     * POST's the neutral rating given to the viewed tenant to the backend
     */
    @Override
    public void swipedDown() {
        if (tenantInfo.size() > 0) {
            Log.i("extra_debug", "Neutral Rating");
        }
    }

}