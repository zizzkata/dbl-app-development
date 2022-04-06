package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.util.view_validation.scenarios.AuthenticationScenario;

public final class currPasswordValidator extends TextPatternValidator {
    public currPasswordValidator(View currPassword, TextView warning) {
        super(currPassword, warning);
        scenarios.add(new AuthenticationScenario((TextView) currPassword, "* Incorrect old password"));
    }
}
