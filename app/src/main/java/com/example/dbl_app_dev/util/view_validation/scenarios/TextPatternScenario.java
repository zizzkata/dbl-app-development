package com.example.dbl_app_dev.util.view_validation.scenarios;

import android.view.View;
import android.widget.TextView;

import java.util.regex.Pattern;

public final class TextPatternScenario extends TextViewScenario {
    Pattern pattern;

    public TextPatternScenario(Pattern pattern, String warning) {
        super(warning);
        this.pattern = pattern;
    }

    /**
     * returns whether a string of text matches a given textual pattern
     *
     * @param textView text view to be checked
     * @return true if {@code textView.toString()} matches {@code this.pattern}, false otherwise
     */
    @Override
    public boolean isValid(View textView) {
        String text = textViewToString((TextView) textView);
        return pattern.matcher(text).matches();
    }
}
