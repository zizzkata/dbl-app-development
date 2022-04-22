package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.util.view_validation.scenarios.ImageScenario;

/**
 * Class that checks validity of an ImageView.
 * Specifically, the image view shall not be null to be valid (the image shall not be missing).
 */
public final class ImageValidator extends ViewValidator {
    public ImageValidator(View toValidate, TextView warning) {
        super(toValidate, warning);

        scenarios.add(new ImageScenario("* Missing image"));
    }
}
