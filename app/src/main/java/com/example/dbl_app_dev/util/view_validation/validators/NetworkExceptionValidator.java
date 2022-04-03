package com.example.dbl_app_dev.util.view_validation.validators;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * Abstract class for Login Exceptions using Firebase.
 */
public abstract class NetworkExceptionValidator {

    protected Exception exception;
    public abstract String getError();

    public NetworkExceptionValidator(@NonNull Exception e) {
        this.exception = e;
    }

    /**
     * Gets the message out of the exception.
     * @return originalException.getMessage();
     */
    protected String getExceptionMessage() {
        String[] strings = exception.getMessage().split(":", 2);
        if (strings.length < 2) {
            return exception.getMessage();
        }
        return strings[1].substring(1);
    }

    /**
     * Gets the exception type out of the exception.
     * @return originalException.getClass();
     */
    protected  String getExceptionType() {
        String[] strings = exception.getMessage().split(":", 2);
        if (strings.length < 2) {
            return exception.getMessage();
        }
        return exception.getMessage().split(":", 1)[0];
    }
}
