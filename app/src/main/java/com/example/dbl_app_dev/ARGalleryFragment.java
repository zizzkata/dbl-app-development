package com.example.dbl_app_dev;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

public class ARGalleryFragment extends Fragment {

    public ARGalleryFragment() {
        super();
    }

    public static ARGalleryFragment newInstance(Bitmap image) {
        ARGalleryFragment fragment = new ARGalleryFragment();
        Bundle args = new Bundle();

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

        Log.d("extra_debug", "AR View Fragment Created");

        assert getArguments() != null;
        Bitmap image = getArguments().getParcelable("image");
        VrPanoramaView mVRPanoramaView = (VrPanoramaView) requireView().findViewById(R.id.vrPanoramaView);

        boolean hasGyroscope = requireContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        // If phone doesn't have a gyroscope, allow vertical drag.
        if (!hasGyroscope) {
            mVRPanoramaView.setPureTouchTracking(true);
        }
        loadPhotoSphere(mVRPanoramaView, image);
        removeButtons(mVRPanoramaView);
    }

    // Loads a jpeg image and maps it to the sphere.
    private void loadPhotoSphere(VrPanoramaView mVRPanoramaView, Bitmap image) {
        VrPanoramaView.Options options = new VrPanoramaView.Options();

        options.inputType = VrPanoramaView.Options.TYPE_MONO;
        mVRPanoramaView.loadImageFromBitmap(image, options);
    }

    // Removes the information button and the cardboard button.
    private void removeButtons(VrPanoramaView mVRPanoramaView) {
        mVRPanoramaView.setInfoButtonEnabled(false);
        mVRPanoramaView.setStereoModeButtonEnabled(false);
    }
}