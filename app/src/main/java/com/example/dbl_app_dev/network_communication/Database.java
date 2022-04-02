package com.example.dbl_app_dev.network_communication;

import android.util.Log;

import com.example.dbl_app_dev.util.AsyncWrapper;
import com.google.firebase.firestore.DocumentSnapshot;

public abstract class Database {

    /**
     * Get username of currently authenticated user
     * @pre user is authenticated
     * @param email
     */
    public static String getUsername(String email) throws Exception {
            return (String) AsyncWrapper.wrap(FirebaseQueries.getUsername(email)).get("username");
    }

    /**
     * Get user's information
     * @param username
     * @return
     */
    public static DocumentSnapshot getUserInformation(String username) throws Exception {
            return AsyncWrapper.wrap(FirebaseQueries.getUserInformation(username));
    }
}
