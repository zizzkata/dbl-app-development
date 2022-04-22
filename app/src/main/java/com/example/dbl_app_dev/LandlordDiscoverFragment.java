package com.example.dbl_app_dev;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.dbl_app_dev.store.Store;
import com.example.dbl_app_dev.store.objects.User;
import com.example.dbl_app_dev.util.AsyncWrapper;
import com.example.dbl_app_dev.util.CardSwipeListener;
import com.example.dbl_app_dev.util.SwipeHandler;

/**
 * Discovery page fragment, if the user is in "Landlord" mode
 */
public class LandlordDiscoverFragment extends Fragment implements SwipeHandler {

    private GestureDetector swipeListener;
    private User currentTenantInfo; // currently viewed tenant
    ConstraintLayout noSwipesContainer;
    ConstraintLayout contentContainer;

    private TextView nameTxt;
    private TextView emailTxt;
    private TextView phoneNumberTxt;
    private TextView petsTxt;
    private TextView smokerTxt;
    private TextView descriptionTxt;
    private ImageView imageView;
    private TextView genderTxt;

    public LandlordDiscoverFragment() {
        // Required empty public constructor
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

        ConstraintLayout topCard = view.findViewById(R.id.topCard);

        nameTxt = view.findViewById(R.id.nameTxt);
        emailTxt = view.findViewById(R.id.emailTxt);
        genderTxt = view.findViewById(R.id.genderTxt);
        phoneNumberTxt = view.findViewById(R.id.phoneNumberTxt);
        petsTxt = view.findViewById(R.id.petsTxt);
        smokerTxt = view.findViewById(R.id.smokerTxt);
        descriptionTxt = view.findViewById(R.id.descriptionTxt);
        imageView = view.findViewById(R.id.tenantPicture);

        noSwipesContainer = view.findViewById(R.id.noSwipesContainer);
        contentContainer = view.findViewById(R.id.contentContainer);
        noSwipesContainer.setVisibility(View.INVISIBLE);
        contentContainer.setVisibility(View.VISIBLE);

        this.swipeListener = new GestureDetector(getContext(),
                new CardSwipeListener(this, true, true));
        topCard.setOnTouchListener((v, event) -> {
            if (swipeListener.onTouchEvent(event)) {
                displayCard(true);
                return false;
            }
            return true;
        });

        Button likeBtn = view.findViewById(R.id.positiveButton);
        Button dislikeBtn = view.findViewById(R.id.negativeButton);
        Button neutralBtn = view.findViewById(R.id.neutralButton);

        likeBtn.setOnClickListener(v -> {
            swipedRight();
            displayCard(true);
        });
        dislikeBtn.setOnClickListener(v -> {
            swipedLeft();
            displayCard(true);
        });
        neutralBtn.setOnClickListener(v -> {
            swipedDown();
            displayCard(true);
        });

        displayCard(currentTenantInfo == null);

    }

    /**
     * Displays the information stored in currentAccommodationInfo
     */
    private void displayCard(boolean nextCard) {
        AsyncWrapper.wrap(() -> {
            try {
                if (nextCard) {
                    currentTenantInfo = Store.getUserToBeRated();
                }
                getActivity().runOnUiThread(() -> {
                    noSwipesContainer.setVisibility(View.INVISIBLE);
                    contentContainer.setVisibility(View.VISIBLE);
                    bindData(currentTenantInfo);
                });

            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    noSwipesContainer.setVisibility(View.VISIBLE);
                    contentContainer.setVisibility(View.INVISIBLE);
                });
                Log.i("Error", e.getMessage());
            }
        });
    }

    /**
     * POST's the positive rating given to the viewed tenant to the backend
     */
    @Override
    public void swipedRight() {
        Log.i("extra_debug", "Positive Rating");
    }

    /**
     * POST's the negative rating given to the viewed tenant to the backend
     */
    @Override
    public void swipedLeft() {
        Log.i("extra_debug", "Negative Rating");
    }

    /**
     * POST's the neutral rating given to the viewed tenant to the backend
     */
    @Override
    public void swipedDown() {
        Log.i("extra_debug", "Neutral Rating");
    }

    public void bindData(User data) {
        nameTxt.setText(data.getFirstName() + " " + data.getLastName());
        emailTxt.setText("Username: " + data.getUsername());
        phoneNumberTxt.setText("Phone number: " + data.getPhoneNumber());
        petsTxt.setText("Pet owner: " + booleanToYesNo(data.hasPets()));
        smokerTxt.setText("Smoker: " + booleanToYesNo(data.smokes()));
        descriptionTxt.setText(data.getDescription());
        genderTxt.setText("Gender: " + data.getGender());
        imageView.setImageBitmap(data.getProfilePic());
    }

    private String booleanToYesNo(boolean x) {
        if (x) {
            return "Yes";
        } else {
            return "No";
        }
    }

}