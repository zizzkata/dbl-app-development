package com.example.dbl_app_dev.util.view_validation.scenarios;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.network_communication.Authentication;
import com.example.dbl_app_dev.util.AsyncWrapper;

public final class UsernameUniquenessScenario extends TextViewScenario {

    // To be used if server cannot be queried for username uniqueness
    private final String NETWORK_ERROR = "* Error contacting the server";

    public UsernameUniquenessScenario(String warning) {
        super(warning);
    }

    @Override
    public boolean isValid(View view) {
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
