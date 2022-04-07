package com.example.dbl_app_dev;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
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

import com.example.dbl_app_dev.store.Store;
import com.example.dbl_app_dev.store.objects.AccommodationInfo;
import com.example.dbl_app_dev.store.objects.User;
import com.example.dbl_app_dev.util.AsyncWrapper;

import java.util.ArrayList;

/**
 * Discovery page fragment, if the user is in "Tenant" mode
 * <p>
 * Use the {@link TenantDiscoverFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class TenantDiscoverFragment extends Fragment implements SwipeHandler {
    private GestureDetector horizontalSwipeDetector;
    private AccommodationInfo currentAccommodationInfo = null; // currently viewed accommodation
    VerticalViewPager imageGalleryViewPager;
    ConstraintLayout noSwipesContainer;
    ConstraintLayout contentContainer;
    ImageViewPagerAdapter verticalViewPagerAdapter;

    private TextView addressTxt;
    private TextView floorTxt;
    private TextView postcodeTxt;
    private TextView priceTxt;
    private TextView accommTypeTxt;
    private TextView utilitiesTxt;
    private TextView areaTxt;
    private TextView furnishedTxt;
    private TextView petsTxt;
    private TextView smokersTxt;
    private TextView minimumPeriodTxt;
    private TextView availableTxt;
    private TextView untilTxt;
    private TextView descriptionTxt;

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
        addressTxt = view.findViewById(R.id.addressTxt);
        floorTxt = view.findViewById(R.id.floorTxt);
        postcodeTxt = view.findViewById(R.id.postcodeTxt);
        priceTxt = view.findViewById(R.id.priceTxt);
        accommTypeTxt = view.findViewById(R.id.accommTypeTxt);
        utilitiesTxt = view.findViewById(R.id.utilitiesTxt);
        areaTxt = view.findViewById(R.id.areaTxt);
        furnishedTxt = view.findViewById(R.id.furnishedTxt);
        petsTxt = view.findViewById(R.id.petsTxt);
        smokersTxt = view.findViewById(R.id.smokersTxt);
        minimumPeriodTxt = view.findViewById(R.id.minimumPeriodTxt);
        availableTxt = view.findViewById(R.id.availableTxt);
        untilTxt = view.findViewById(R.id.untilTxt);
        descriptionTxt = view.findViewById(R.id.descriptionTxt);

        imageGalleryViewPager = view.findViewById(R.id.accommodationImageScroller);

        noSwipesContainer = view.findViewById(R.id.noSwipesContainer);
        contentContainer = view.findViewById(R.id.contentContainer);
        noSwipesContainer.setVisibility(View.INVISIBLE);
        contentContainer.setVisibility(View.VISIBLE);

        this.horizontalSwipeDetector = new GestureDetector(getContext(), new CardSwipeListener(this, true, false));
        topCard.setOnTouchListener((v, event) -> {
            if (horizontalSwipeDetector.onTouchEvent(event)) {
                nextCard();
                return false;
            }
            return true;
        });

        Button likeBtn = view.findViewById(R.id.positiveButton);
        Button dislikeBtn = view.findViewById(R.id.negativeButton);
        Button arBtn = view.findViewById(R.id.arButton);

        likeBtn.setOnClickListener(v -> {
            swipedRight();
            nextCard();
        });
        dislikeBtn.setOnClickListener(v -> {
            swipedLeft();
            nextCard();
        });
        arBtn.setOnClickListener(v -> {
            imageGalleryViewPager.setAdapter(verticalViewPagerAdapter);
        });
        arBtn.setOnClickListener(v -> {
            imageGalleryViewPager.setAdapter(verticalViewPagerAdapter);
        });

        Button filtersBtn = view.findViewById(R.id.filtersButton);
        filtersBtn.setOnClickListener(v -> ((MainNavigationActivity) requireActivity()).openFilterDialog());

        // makes sure that the a card is not discarded if it is not rated
        if (currentAccommodationInfo == null) {
            nextCard();
        }

        // open pop-up
        AsyncWrapper.wrap(() -> {
            try {
                User user = Store.getCurrentUser();
                if (user.getFirstName().length() == 0 || user.getLastName().length() == 0
                        || user.getDescription().length() == 0)
                    getActivity().runOnUiThread(() -> ((MainNavigationActivity) getActivity()).openSettingsDialog());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Updates the information on currentAccommodationInfo and displays it
     */
    private void nextCard() {
        AsyncWrapper.wrap(() -> {
            try {
                currentAccommodationInfo = Store.getNextAccommodation();
                getActivity().runOnUiThread(() -> {
                    bindData(currentAccommodationInfo);
                });
                Bitmap pan = currentAccommodationInfo.getPhotoPanoramic();
                ArrayList<Bitmap> n = currentAccommodationInfo.getPhotos();
                getActivity().runOnUiThread(() -> {
                    verticalViewPagerAdapter = new ImageViewPagerAdapter(getChildFragmentManager(), n, pan);
                    imageGalleryViewPager.setAdapter(verticalViewPagerAdapter);
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERR", e.getMessage());
            }
        });

    }

    /**
     * POST's the positive rating given to the viewed accommodation to the backend
     */
    @Override
    public void swipedRight() {
        // if (dataModels.size() > 0) {
        // Log.d("extra_debug", "Positive Rating");
        // }
    }

    /**
     * POST's the negative rating given to the viewed accommodation to the backend
     */
    @Override
    public void swipedLeft() {
        // if (dataModels.size() > 0) {
        // Log.d("extra_debug", "Negative Rating");
        // }
    }

    private void bindData(AccommodationInfo data) {
        addressTxt.setText(data.getAddress());
        postcodeTxt.setText(data.getDescription());
        floorTxt.setText(data.getFloor());
    }
}
