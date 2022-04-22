package com.example.dbl_app_dev.util.view_validation.scenarios;

import android.widget.TextView;

/**
 * Class that that is a generic scenario used for checking validity of text views
 * All scenarios that validate text views should inherit from this scenario
 */
public abstract class TextViewScenario extends WarningScenario {
    protected TextViewScenario(String warning) {
        super(warning);
    }

    /**
     * Returns text inside of the given text view
     *
     * @param textView the given text view
     * @return text inside of the text view
     */
    protected String textViewToString(TextView textView) {
        return textView.getText().toString();
    }
}
