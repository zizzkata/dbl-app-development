package com.example.dbl_app_dev.util.view_validation.scenarios;

import android.view.View;
import android.widget.TextView;

public final class TextMatchingScenario extends TextViewScenario {
    TextView textView;

    public TextMatchingScenario(TextView textView, String warning) {
        super(warning);
        this.textView = textView;
    }

    /**
     * returns whether a string of text is identical (matches) to another string
     *
     * @param textView text view to be checked
     * @return true if {@code this.text.equals(textView.toString())}, false otherwise
     */
    @Override
    public boolean isValid(View textView) {
        String textToMatch = textViewToString((TextView) textView);
        String currText = textViewToString((TextView) this.textView);
        return currText.equals(textToMatch);
    }
}
