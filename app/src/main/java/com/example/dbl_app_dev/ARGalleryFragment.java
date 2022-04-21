package com.example.dbl_app_dev;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

/**
 * Fragment used to display a panoramic/AR image
 */
public class ARGalleryFragment extends Fragment {

    public ARGalleryFragment() {
        super();
    }

    public static ARGalleryFragment newInstance(Bitmap image) {
        // Create the fragment and bundle
        ARGalleryFragment fragment = new ARGalleryFragment();
        Bundle args = new Bundle();

        // Save the image in the parcelable
        // NOTE: parcelable has 1MB file limit
        args.putParcelable("image", image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        // Get the image from the parcelable arguments
        Bitmap image = getArguments().getParcelable("image");
        VrPanoramaView mVRPanoramaView = (VrPanoramaView)
                requireView().findViewById(R.id.vrPanoramaView);

        // Check if the phone has a gyroscope
        boolean hasGyroscope = requireContext()
                .getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);

        // If phone doesn't have a gyroscope, allow vertical drag.
        if (!hasGyroscope) {
            mVRPanoramaView.setPureTouchTracking(true);
        }

        // Then load the photosphere
        loadPhotoSphere(mVRPanoramaView, image);

        // Finally disable some buttons' functionality
        removeButtons(mVRPanoramaView);
    }

    // Loads a jpeg image and maps it to the sphere.
    private void loadPhotoSphere(VrPanoramaView mVRPanoramaView, Bitmap image) {
        VrPanoramaView.Options options = new VrPanoramaView.Options();

        // Set the input type and load the image
        options.inputType = VrPanoramaView.Options.TYPE_MONO;
        mVRPanoramaView.loadImageFromBitmap(image, options);
    }

    // Removes the information button and the cardboard button.
    private void removeButtons(VrPanoramaView mVRPanoramaView) {
        mVRPanoramaView.setInfoButtonEnabled(false);
        mVRPanoramaView.setStereoModeButtonEnabled(false);
    }
}