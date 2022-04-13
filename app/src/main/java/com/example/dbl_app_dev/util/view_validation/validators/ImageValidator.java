package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.util.view_validation.scenarios.ImageScenario;

public final class ImageValidator extends ViewValidator {
    public ImageValidator(View toValidate, TextView warning) {
        super(toValidate, warning);

        scenarios.add(new ImageScenario("* Missing image"));
    }
}
