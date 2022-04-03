package com.example.dbl_app_dev.util.view_validation.scenarios;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.dbl_app_dev.util.view_validation.constants.Exceptions;
import com.example.dbl_app_dev.util.view_validation.validators.NetworkExceptionValidator;

/**
 * Finds out what the error is and returns a user friendly error message.
 */
public class NetworkLoginErrorScenario extends NetworkExceptionValidator {

    /** Messages to be  shown to the user on the UI screen */
    public final static String INVALID_PASSWORD = "* Invalid password";
    public final static String INVALID_USER = "* No such user exists. Please first sign-up";
    public final static String NETWORK_ERROR = "* There is no Internet connection.";
    public final static String EMAIL_FORMAT_ERROR = "* Email format is invalid";

    public NetworkLoginErrorScenario(@NonNull Exception e) {
        super(e);
    }

    @Override
    public String getError() {
        String errorRaw = super.getExceptionMessage();
        if (errorRaw.equals(Exceptions.INVALID_PASSWORD_MESSAGE)) {
            return INVALID_PASSWORD;
        } else if (errorRaw.equals(Exceptions.INVALID_USER_MESSAGE)) {
            return INVALID_USER;
        } else if (errorRaw.equals(Exceptions.NETWORK_ERROR_MESSAGE)) {
            return NETWORK_ERROR;
        } else if (errorRaw.equals(Exceptions.INVALID_EMAIL_FORMAT_MESSAGE)) {
            return EMAIL_FORMAT_ERROR;
        } else {
            return errorRaw;
        }
    }
}
