package com.example.dbl_app_dev.store;


import com.example.dbl_app_dev.network_communication.Authentication;
import com.example.dbl_app_dev.store.objects.User;
import com.google.firebase.auth.FirebaseUser;

/**
 * Class that acts as local cache.
 */
public final class Store {

    /**
     * States and variables that need to be saved locally
     */
    private static User currentUser;
    private static Exception lastExecutedException;

    private Store() throws Exception {
        throw new Exception("Cannot initialize this class.");
    }

    /**
     * Login using firebase and save it to the store
     *
     * @param email
     * @param password
     * @return true if successful else false;
     */
    public static boolean login(String email, String password) {
        FirebaseUser auth;
        try {
            auth = Authentication.firebaseLogin(email, password).getUser();
            currentUser = new User(auth);
        } catch (Exception e) {
            lastExecutedException = e;
            return false;
        }
        lastExecutedException = null;
        return true;
    }

    /**
     * Signup using firebase.
     *
     * @param email
     * @param password
     * @param username
     * @return true if successful else false;
     */
    public static boolean signup(String email, String password, String username) {
        try {
            Authentication.firebaseSignup(email, password, username);
        } catch (Exception e) {
            lastExecutedException = e;
            return false;
        }
        lastExecutedException = null;
        return true;
    }

    /**
     * Get the current user's information
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Get exception from the last transaction
     */
    public static Exception getLastException() {
        return lastExecutedException;
    }
}
