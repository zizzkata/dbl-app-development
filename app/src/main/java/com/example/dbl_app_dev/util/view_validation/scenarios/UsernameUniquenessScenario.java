package com.example.dbl_app_dev.util.view_validation.scenarios;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.network_communication.Authentication;

/**
 * Class that explicitly handles the uniquess of the username, and shows a textual warning if
 * the username is not unique (already taken) in the database
 *
 * In case communication with firebase cannot be established, it shows a warning that the
 * server could not be contacted (i.e. the phone is not connected to the internet)
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
