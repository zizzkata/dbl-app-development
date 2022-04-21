package com.example.dbl_app_dev;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ImageGalleryFragment extends Fragment {

    public ImageGalleryFragment() {
        super();
    }

    public static ImageGalleryFragment newInstance(Bitmap image) {
        ImageGalleryFragment fragment = new ImageGalleryFragment();
        Bundle args = new Bundle();

        // NOTE: parcelable has 1MB file limit
        args.putParcelable("image", image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView accommodationImageView = requireView().findViewById(R.id.accommodationImageView);
        Log.d("extra_debug", "Image View Fragment Created");

        Bitmap image = getArguments().getParcelable("image");
        accommodationImageView.setImageBitmap(image);
    }
}