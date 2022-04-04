package com.example.dbl_app_dev.util.view_validation.scenarios;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.network_communication.Authentication;
import com.example.dbl_app_dev.util.AsyncWrapper;

public class NonUniqueUsernameScenario extends TextViewScenario {

    private final String NETWORK_ERROR = "Error contacting the server.";
    private String genericWarning;

    public NonUniqueUsernameScenario(String warning) {
        super(warning);
        this.genericWarning = warning;
    }

    @Override
    public boolean isValid(View view) {
        super.warningText = genericWarning;
        try {
            return Authentication.isUsernameUnique(textViewToString((TextView)
                    view));
        } catch (Exception e) {
            super.warningText = NETWORK_ERROR;
            Log.e("isUsernameUnique", e.getMessage());
        }
        return false;
    }
}
