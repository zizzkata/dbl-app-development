package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.util.view_validation.scenarios.NonUniqueUsernameScenario;
import com.example.dbl_app_dev.util.view_validation.scenarios.TextPatternScenario;

import java.util.regex.Pattern;

public final class UsernameValidator extends TextPatternValidator {

    private NonUniqueUsernameScenario networkError = new NonUniqueUsernameScenario(
            "* User with this username already exists");

    public UsernameValidator(View toValidate, TextView warning) {
        super(toValidate, warning);

        final Pattern USERNAME_SHORT = getPatternFromRegex("^.{4,}$");
        final Pattern USERNAME_LONG = getPatternFromRegex("^.{0,12}$");

        scenarios.add(new TextPatternScenario(USERNAME_SHORT, "* Username too short"));
        scenarios.add(new TextPatternScenario(USERNAME_LONG, "* Username too long"));
        scenarios.add(new NonUniqueUsernameScenario("* There is user with" +
                " this username already"));
    }

    /**
     * For one time validation.
     * @return super.isValid
     */
    public boolean isUsernameUnique() {
        validate(networkError);
        return isValid;
    }

}
