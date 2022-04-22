package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * Generic validator for TextViews. Inherit from this class when creating a new TextView validator.
 */
public abstract class TextPatternValidator extends ViewValidator {

    protected TextPatternValidator(View toValidate, TextView warning) {
        super(toValidate, warning);
    }

    /**
     * Returns a Pattern object for a given regex string
     *
     * @param regex regex string
     * @return Pattern object for given regex string
     */
    protected Pattern getPatternFromRegex(final String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern;
    }
}
