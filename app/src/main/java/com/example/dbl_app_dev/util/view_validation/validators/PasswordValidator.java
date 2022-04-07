package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.util.view_validation.scenarios.TextPatternScenario;

import java.util.regex.Pattern;

public class PasswordValidator extends TextPatternValidator {
    public PasswordValidator(View toValidate, TextView warning) {
        super(toValidate, warning);

        final Pattern PASSWORD_SHORT = getPatternFromRegex(".{8,}");
        final Pattern PASSWORD_LOWERCASE = getPatternFromRegex(".*[a-z].*");
        final Pattern PASSWORD_UPPERCASE = getPatternFromRegex(".*[A-Z].*");
        final Pattern PASSWORD_DIGIT = getPatternFromRegex(".*\\d.*");
        final Pattern PASSWORD_SPECIAL = getPatternFromRegex(".*\\W.*");
        scenarios.add(new TextPatternScenario(PASSWORD_SHORT, "* Password too short"));
        scenarios.add(new TextPatternScenario(PASSWORD_LOWERCASE, "* Password requires lowercase character"));
        scenarios.add(new TextPatternScenario(PASSWORD_UPPERCASE, "* Password requires uppercase character"));
        scenarios.add(new TextPatternScenario(PASSWORD_DIGIT, "* Password requires digit"));
        scenarios.add(new TextPatternScenario(PASSWORD_SPECIAL, "* Password requires special character"));
    }
}
