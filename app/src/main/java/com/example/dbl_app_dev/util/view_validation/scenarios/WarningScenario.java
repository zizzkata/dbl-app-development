package com.example.dbl_app_dev.util.view_validation.scenarios;

import android.view.View;

/**
 * A warning scenario
 * If the scenario is evaluated to invalid, {@code warningText}
 * shall be displayed somewhere on the current activity
 * <p>
 * Every view validator has a list of scenarios of at least length 1
 */
public abstract class WarningScenario {
    protected String warningText;

    protected WarningScenario(String warning) {
        this.warningText = warning;
    }

    public String getWarningText() {
        return warningText;
    }

    public abstract boolean isValid(View view);
}