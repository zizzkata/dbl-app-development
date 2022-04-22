package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.util.view_validation.scenarios.AuthenticationScenario;

/**
 * Class that checks validity of a "current password" TextView. Specifically, the password
 * in the TextView shall match the one in the database to be valid.
 */
public final class currPasswordValidator extends TextPatternValidator {
    public currPasswordValidator(View currPassword, TextView warning) {
        super(currPassword, warning);
        scenarios.add(new AuthenticationScenario((TextView) currPassword, "* Incorrect old password"));
    }
}
