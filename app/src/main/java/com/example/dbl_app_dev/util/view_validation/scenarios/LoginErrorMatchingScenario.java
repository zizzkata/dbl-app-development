package com.example.dbl_app_dev.util.view_validation.scenarios;

import android.view.View;

public class LoginErrorMatchingScenario extends TextViewScenario {

    public LoginErrorMatchingScenario(String errorMessage) {
        super(errorMessage);
    }

    public LoginErrorMatchingScenario(Exception error) {
        super(error.getMessage());
    }

    public boolean checkError() {
        return false;
    }

    @Override
    public String getWarningText() {
        String exceptionMessage = "";
        if (warningText == "") {

        } else if (warningText == "") {

        } else if (warningText == "") {

        } else { // prob a network prob

        }

        return exceptionMessage;
    }
    @Override
    public boolean isValid(View view) {
        return false;
    }
}
