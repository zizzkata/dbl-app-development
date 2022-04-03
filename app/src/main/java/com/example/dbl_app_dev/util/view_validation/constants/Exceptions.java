package com.example.dbl_app_dev.util.view_validation.constants;

public class Exceptions {
    public final static String INVALID_USER = "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.";
    public final static String INVALID_USER_MESSAGE = "There is no user record corresponding to this identifier. The user may have been deleted.";
    public final static String INVALID_PASSWORD = "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.";
    public final static String INVALID_PASSWORD_MESSAGE = "The password is invalid or the user does not have a password.";
    public final static String INVALID_EMAIL_FORMAT = "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.";
    public final static String INVALID_EMAIL_FORMAT_MESSAGE = "The email address is badly formatted.";
    public final static String NETWORK_ERROR = "com.google.firebase.FirebaseException: An internal error has occurred. [ Connection reset ]";
    public final static String NETWORK_ERROR_MESSAGE = "An internal error has occurred. [ Connection reset ]";
    public final static String PERMISSION_DENIED_MESSAGE = "PERMISSION_DENIED: Missing or insufficient permissions.";
    public final static String PERMISSION_DENIED = "com.google.firebase.firestore.FirebaseFirestoreException: PERMISSION_DENIED: Missing or insufficient permissions.";
}
