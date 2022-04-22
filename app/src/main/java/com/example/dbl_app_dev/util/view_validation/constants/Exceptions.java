package com.example.dbl_app_dev.util.view_validation.constants;

import java.util.HashMap;

/**
 * Class that holds String constants for common exceptions relating to the Firebase database
 */
public class Exceptions {
    // Definition of all commonly thrown exceptions
    private final static String INVALID_USER = "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.";
    private final static String INVALID_USER_MESSAGE = "* No such user exists. Please first sign-up";
    private final static String INVALID_PASSWORD = "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.";
    private final static String INVALID_PASSWORD_MESSAGE = "* Invalid password";
    private final static String NON_UNIQUE_EMAIL = "com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.";
    private final static String NON_UNIQUE_EMAIL_MESSAGE = "The email address is already in use by another account.";
    private final static String INVALID_EMAIL_FORMAT = "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.";
    private final static String INVALID_EMAIL_FORMAT_MESSAGE = "* Email format is invalid";
    private final static String NETWORK_ERROR = "com.google.firebase.FirebaseException: An internal error has occurred. [ Connection reset ]";
    private final static String NETWORK_ERROR_MESSAGE = "* There is no Internet connection.";
    private final static String PERMISSION_DENIED = "com.google.firebase.firestore.FirebaseFirestoreException: PERMISSION_DENIED: Missing or insufficient permissions.";
    private final static String PERMISSION_DENIED_MESSAGE = "* Fatal error. Please contact the administrator.";

    /**
     * @param exception String representing the exception that was thrown
     * @return String containing a more readable warning
     */
    public static String getWarning(String exception) {
        HashMap<String, String> exceptionToWarning = new HashMap<>();
        exceptionToWarning.put(INVALID_USER, INVALID_USER_MESSAGE);
        exceptionToWarning.put(INVALID_PASSWORD, INVALID_PASSWORD_MESSAGE);
        exceptionToWarning.put(NON_UNIQUE_EMAIL, NON_UNIQUE_EMAIL_MESSAGE);
        exceptionToWarning.put(INVALID_EMAIL_FORMAT, INVALID_EMAIL_FORMAT_MESSAGE);
        exceptionToWarning.put(NETWORK_ERROR, NETWORK_ERROR_MESSAGE);
        exceptionToWarning.put(PERMISSION_DENIED, PERMISSION_DENIED_MESSAGE);

        // if exception is not part of the commonly thrown exceptions, do not covert it to a warning
        // instead, return it as-is
        return exceptionToWarning.containsKey(exception) ? exceptionToWarning.get(exception) : exception;
    }
}
