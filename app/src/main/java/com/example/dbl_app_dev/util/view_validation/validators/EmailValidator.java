package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

import java.util.regex.Pattern;

import com.example.dbl_app_dev.util.view_validation.scenarios.TextPatternScenario;

public final class EmailValidator extends TextPatternValidator {

    public EmailValidator(View toValidate, TextView warning) {
        super(toValidate, warning);

        final Pattern EMAIL = android.util.Patterns.EMAIL_ADDRESS;
        scenarios.add(new TextPatternScenario(EMAIL, "* Invalid e-mail"));
    }
}
