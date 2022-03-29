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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Discovery page fragment, if the user is in "Landlord" mode
 * <p>
 * Use the {@link LandlordDiscoverFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandlordDiscoverFragment extends Fragment implements SwipeHandler {

    private final Deque<TenantInfo> tenantInfo = new LinkedList<>();
    private GestureDetector swipeListener;
    private TenantInfo currentTenantInfo = null;    // currently viewed tenant

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
        TextView nameTxt = view.findViewById(R.id.tenantName);
        TextView descriptionTxt = view.findViewById(R.id.tenantDescription);
        ImageView imageView = view.findViewById(R.id.tenantPicture);

        // makes sure that the a card is not discarded if it is not rated
        if (currentTenantInfo == null) {
            nextCard(nameTxt, imageView, descriptionTxt);
        } else {
            displayCard(nameTxt, imageView, descriptionTxt);
        }

        this.swipeListener = new GestureDetector(getContext(), new CardSwipeListener(this, true, true));
        topCard.setOnTouchListener((v, event) -> {
            if (swipeListener.onTouchEvent(event)) {
                nextCard(nameTxt, imageView, descriptionTxt);
            }
            return true;
        });

        Button likeBtn = view.findViewById(R.id.tenantLikeBtn);
        Button dislikeBtn = view.findViewById(R.id.tenantDislikeBtn);
        Button neutralBtn = view.findViewById(R.id.tenantNeutralBtn);
        likeBtn.setOnClickListener(v -> {
            swipedRight();
            nextCard(nameTxt, imageView, descriptionTxt);
        });
        dislikeBtn.setOnClickListener(v -> {
            swipedLeft();
            nextCard(nameTxt, imageView, descriptionTxt);
        });
        neutralBtn.setOnClickListener(v -> {
            swipedDown();
            nextCard(nameTxt, imageView, descriptionTxt);
        });
    }

    /**
     * Displays the information stored in currentAccommodationInfo
     */
    private void displayCard(TextView cardTitle, ImageView cardImage, TextView cardDescription) {
        cardTitle.setText(currentTenantInfo.getName());
        cardDescription.setText(currentTenantInfo.getDescription());
        cardImage.setImageBitmap(currentTenantInfo.getPhoto());
    }

    private void nextCard(TextView cardTitle, ImageView cardImage, TextView cardDescription) {
        if (tenantInfo.size() > 0) {
            currentTenantInfo = this.tenantInfo.remove();
            displayCard(cardTitle, cardImage, cardDescription);
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
        // TODO: remove placeholder code, get data from server
        for (int i = 0; i < batchSize; i++) {
            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.default_tenant_picture);
            tenantInfo.add(new TenantInfo(String.format("John Doe %d", i), String.format("Description %d", i), image, 21 + i));
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