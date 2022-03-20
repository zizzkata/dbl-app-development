package com.example.dbl_app_dev;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Discovery page if the user is acting as landlord
 */
public class DiscoveryLandlordPage extends AppCompatActivity implements RatingHandler {

    private Queue<TenantInfo> tenantInfo = new LinkedList<>();
    private GestureDetector gestureDetector;
    private TenantInfo currentTenantInfo;    // currently viewed tenant

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_landlord);

        pullCardsInfo(10);
        ConstraintLayout topCard = findViewById(R.id.topCard);
        TextView nameTxt = findViewById(R.id.tenantName);
        TextView descriptionTxt = findViewById(R.id.tenantDescription);
        ImageView imageView = findViewById(R.id.tenantPicture);
        nextCard(nameTxt, imageView, descriptionTxt);

        this.gestureDetector = new GestureDetector(this, new CardSwipeListener(this));
        topCard.setOnTouchListener((v, event) -> {
            if (gestureDetector.onTouchEvent(event)) {
                nextCard(nameTxt, imageView, descriptionTxt);
            }
            return true;
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
        Log.i("extra_debug", "Positive Rating");
    }

    /**
     * POST's the negative rating given to the viewed tenant to the backend
     */
    @Override
    public void negativeRating() {
        Log.i("extra_debug", "Negative Rating");
    }

    /**
     * POST's the neutral rating given to the viewed tenant to the backend
     */
    @Override
    public void neutralRating() {
        Log.i("extra_debug", "Neutral Rating");
    }
}
