package com.example.dbl_app_dev.network_communication;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;

public abstract class FirebaseQueries {
    /** firebase db */
    private static FirebaseFirestore fireStore = FirebaseFirestore
            .getInstance(Authentication.getApp());

    /** db collections */
    private static CollectionReference users = fireStore.collection("users");
    private static CollectionReference identity = fireStore.collection("identity");

    /**
     * Get identity snapshot by providing an email
     * @param email
     * @pre user needs to be authenticated
     * @return
     */
    public static Task<DocumentSnapshot> getUsername(String email) {
        Log.d("getUsernameByEmail", "Email: " + email);
        return identity.document(email).get();
    }

    /**
     * Get user snapshot by providing an email
     * @param username
     * @return
     */
    public static Task<DocumentSnapshot> getUserInformation(String username) {
        Log.d("getUserByUsername", "Username: " + username);
        return users.document(username).get();
    }

    /**
     * Update personal information
     * @param username
     * @param data
     * @pre user needs to be authenticated
     * @return
     */
    public static Task updateUserInformation(String username, @NonNull Map data) {
        Log.d("updateUserInformation", "Username: " + username);
        return users.document(username).update(data);
    }

}