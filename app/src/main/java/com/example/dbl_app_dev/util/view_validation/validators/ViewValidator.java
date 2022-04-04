package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.util.view_validation.scenarios.WarningScenario;

import java.util.ArrayList;

/**
 * class that is the superclass of all validators
 * inherit from this class if you want to create a new validator
 */
public abstract class ViewValidator {

    protected boolean isValid;
    protected View toValidate;
    protected TextView warning;

    protected ArrayList<WarningScenario> scenarios;

    protected ViewValidator() {
    }

    protected ViewValidator(View toValidate, TextView warning) {
        this.toValidate = toValidate;
        this.warning = warning;
        this.isValid = false;

        scenarios = new ArrayList<>();
    }

    protected String getWarningText() {
        for (WarningScenario s : scenarios) {

            // if scenario is valid, check next scenario
            if (s.isValid(toValidate)) {
                continue;
            }

            // if scenario is invalid, return warning text for said scenario
            return s.getWarningText();
        }

        return null;
    }

    protected String getWarningText(WarningScenario scenario) {
        if (!scenario.isValid(toValidate)) {
            // if scenario is invalid, return warning text for said scenario
            return scenario.getWarningText();
        }
        return null;
    }

    /**
     * updates the warning text accordingly if a scenario is found invalid
     * updates {@code this.isValid}
     */
    public final void validate() {
        String warningText = getWarningText();

        // set warning text to {@code warningText} if {@code warningText != null}
        warning.setText(warningText == null ? "No warning" : warningText);

        // set validity to true if {@code warningText == null}, and false otherwise
        isValid = warningText == null ? true : false;

        // make warning visible only if {@code isValid == false}
        warning.setVisibility(isValid ? View.GONE : View.VISIBLE);
    }

    /**
     * updates the warning text accordingly if the specified scenario is found invalid
     * updates {@code this.isValid}
     */
    public final void validate(WarningScenario scenario) {
        String warningText = getWarningText(scenario);

        // set warning text to {@code warningText} if {@code warningText != null}
        warning.setText(warningText == null ? "No warning" : warningText);

        // set validity to true if {@code warningText == null}, and false otherwise
        isValid = warningText == null ? true : false;

        // make warning visible only if {@code isValid == false}
        warning.setVisibility(isValid ? View.GONE : View.VISIBLE);
    }

    public final boolean isValid() {
        return isValid;
    }
}
