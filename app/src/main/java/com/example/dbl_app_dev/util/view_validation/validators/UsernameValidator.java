package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.util.view_validation.scenarios.UsernameUniquenessScenario;
import com.example.dbl_app_dev.util.view_validation.scenarios.TextPatternScenario;

import java.util.regex.Pattern;

public class UsernameValidator extends TextPatternValidator {
    public UsernameValidator(View toValidate, TextView warning) {
        super(toValidate, warning);

        final Pattern USERNAME_SHORT = getPatternFromRegex("^.{4,}$");
        final Pattern USERNAME_LONG = getPatternFromRegex("^.{0,12}$");

        scenarios.add(new TextPatternScenario(USERNAME_SHORT, "* Username too short"));
        scenarios.add(new TextPatternScenario(USERNAME_LONG, "* Username too long"));

    }
}
