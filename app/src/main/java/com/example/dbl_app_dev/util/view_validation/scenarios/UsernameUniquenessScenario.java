package com.example.dbl_app_dev.util.view_validation.scenarios;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.network_communication.Authentication;

/**
 * Class that explicitly handles the uniquess of the username, and returns a warning if the
 * server was not able to reply to the query
 */
public final class UsernameUniquenessScenario extends TextViewScenario {

    public UsernameUniquenessScenario(String warning) {
        super(warning);
    }

    @Override
    public boolean isValid(View view) {
        try {
            return Authentication.isUsernameUnique(textViewToString((TextView)
                    view));
        } catch (Exception e) {
            // To be used if server cannot be queried for username uniqueness
            super.warningText = "* Error contacting the server";
            Log.e("isUsernameUnique", e.getMessage());
        }
        return false;
    }
}
