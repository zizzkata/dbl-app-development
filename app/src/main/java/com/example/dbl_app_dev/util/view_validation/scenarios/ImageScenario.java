package com.example.dbl_app_dev.util.view_validation.scenarios;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Class that contains an image scenario, making sure that the provided image is valid,
 * and setting the appropriate warning text if not
 */
public final class ImageScenario extends WarningScenario {

    public ImageScenario(String warning) {
        super(warning);
    }

    @Override
    public boolean isValid(View imageView) {
        return ((ImageView) imageView).getDrawable() == null ? false : true;
    }
}
