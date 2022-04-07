package com.example.dbl_app_dev.network_communication;

import android.util.Log;
import com.example.dbl_app_dev.util.AsyncWrapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class Authentication {

    /**
     * Firebase Authenticator instance
     */
    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    /**
     * Login method for Firebase
     *
     * @param email
     * @param password
     * @return
     * @throws Exception
     */
    public static AuthResult firebaseLogin(String email, String password) throws Exception {
        return AsyncWrapper.wrap(auth.signInWithEmailAndPassword(email, password));
    }

    /**
     * Registration method for Firebase,
     * which has several fail-safe switches implemented.
     *
     * @param email
     * @param password
     * @param username
     */
    public static void firebaseSignup(String email, String password, String username)
            throws Exception {
        // Register user
        AuthResult res = AsyncWrapper.wrap(auth.createUserWithEmailAndPassword(email, password));

        // Try to create identity file
        try {
            AsyncWrapper.wrap(FirebaseQueries.reserveIdentity(email, username
                    , res.getUser().getUid()));
        } catch (Exception e) {
            Log.e("firebaseSignup(): reserveIdentity", e.getMessage());
            AsyncWrapper.wrapSafe(res.getUser().delete());
            throw e;
        }

        // Try to create user file
        try {
            AsyncWrapper.wrap(FirebaseQueries.createNewUser(username));
        } catch (Exception e) {
            Log.e("firebaseSignup(): createNewUser", e.getMessage());
            AsyncWrapper.wrapSafe(FirebaseQueries.deleteIdentity(email));
            AsyncWrapper.wrapSafe(res.getUser().delete());
            throw e;
        }
    }

    /**
     * Returns if user with this exact username already exists.
     *
     * @param username
     * @return
     * @throws Exception
     */
    public static boolean isUsernameUnique(String username) throws Exception {
        // Invert answer to match functionality
        return !AsyncWrapper.wrap(FirebaseQueries.getUserInformation(username), 1200).exists();
    }

    public static FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public static FirebaseApp getApp() {
        return auth.getApp();
    }
}
