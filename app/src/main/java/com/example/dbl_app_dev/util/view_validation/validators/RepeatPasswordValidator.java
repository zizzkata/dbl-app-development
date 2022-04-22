package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.util.view_validation.scenarios.TextMatchingScenario;

/**
 * Class that checks whether 2 passwords match (are the same).
 */
public final class RepeatPasswordValidator extends ViewValidator {
    public RepeatPasswordValidator(View repeatedPassword, View originalPassword, TextView warning) {
        super(repeatedPassword, warning);

        /**
         * pass original password text to WarningScenario.obj,
         * so that it can be checked whether repeated password and original password are matching
         */
        TextView password = (TextView) originalPassword;
        scenarios.add(new TextMatchingScenario(password, "* Passwords do not match"));
    }
}
