package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.util.view_validation.scenarios.WarningScenario;

import java.util.ArrayList;

/**
 * A ViewValidator is a class used to check whether a View is valid and the information it contains
 * can be sent to the database. It is a class that contains an ArrayList of Scenarios.
 * To check the validity of a view, a view validator shall be attached to said view, then the validate method
 * shall be run, and finally validity of said view is updated. The validity of the view can be
 * accessed via the isValid method. A view is valid if and only if all of the scenarios in the
 * ArrayList return valid.
 *
 * For example, a password text box is valid if and only if the text inside contains at least
 * one uppercase character, at least one digit, etc.
 *
 * This is the superclass of all validators. Inherit from this class if you want to create a new validator
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

    public final boolean isValid() {
        return isValid;
    }
}
