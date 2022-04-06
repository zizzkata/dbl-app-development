package com.example.dbl_app_dev.store;


import com.example.dbl_app_dev.network_communication.Authentication;
import com.example.dbl_app_dev.network_communication.Database;
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
     * Get the current user's information or Download it if there is none.
     */
    public static User getCurrentUser() throws Exception {
        if (currentUser == null) {
            currentUser = new User(Authentication.getCurrentUser());
        }
        return currentUser;
    }

    /**
     * Get exception from the last transaction
     */
    public static Exception getLastException() {
        return lastExecutedException;
    }
}
