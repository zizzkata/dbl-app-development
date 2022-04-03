package com.example.dbl_app_dev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.InputStream;
import android.content.res.AssetManager;

public class activity_ar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        VrPanoramaView mVRPanoramaView = (VrPanoramaView) findViewById(R.id.vrPanoramaView);
        boolean hasGyroscope = getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        // If phone doesn't have a gyroscope, allow vertical drag.
        if (!hasGyroscope) {
            mVRPanoramaView.setPureTouchTracking(true);
        }
        removeButtons(mVRPanoramaView);
        loadPhotoSphere(mVRPanoramaView);
    }

    // Loads a jpeg image and maps it to the sphere.
    private void loadPhotoSphere(VrPanoramaView mVRPanoramaView) {
        VrPanoramaView.Options options = new VrPanoramaView.Options();
        InputStream inputStream = null;

        AssetManager assetManager = getAssets();

        try {
            inputStream = assetManager.open("yosemite.jpg");
            System.out.println(inputStream.toString());
            options.inputType = VrPanoramaView.Options.TYPE_MONO;
            mVRPanoramaView.loadImageFromBitmap(BitmapFactory.decodeStream(inputStream), options);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Removes the information button and the cardboard button.
    private void removeButtons(VrPanoramaView mVRPanoramaView) {
        mVRPanoramaView.setInfoButtonEnabled(false);
        mVRPanoramaView.setStereoModeButtonEnabled(false);
    }
}