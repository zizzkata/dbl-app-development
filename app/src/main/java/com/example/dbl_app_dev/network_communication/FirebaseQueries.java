package com.example.dbl_app_dev.network_communication;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import java.util.HashMap;
import java.util.Map;

public abstract class FirebaseQueries {
    /**
     * firebase db
     */
    private static FirebaseFirestore fireStore = FirebaseFirestore
            .getInstance(Authentication.getApp());

    /**
     * db collections
     */
    private static CollectionReference users = fireStore.collection("users");
    private static CollectionReference identity = fireStore.collection("identity");

    /**
     * Get batch job from Firestore
     *
     * @return Batch
     */
    public static WriteBatch getBatch() {
        return fireStore.batch();
    }

    /**
     * Get identity snapshot by providing an email
     *
     * @param email
     * @return Task<DocumentSnapshot>
     * @pre user needs to be authenticated
     */
    public static Task<DocumentSnapshot> getUsername(String email) {
        Log.d("getUsernameByEmail", "Email: " + email);
        return identity.document(email).get();
    }

    /**
     * Get user snapshot by providing an email
     *
     * @param username
     * @return Task<DocumentSnapshot>
     */
    public static Task<DocumentSnapshot> getUserInformation(String username) {
        Log.d("getUserByUsername", "Username: " + username);
        return users.document(username).get();
    }

    /**
     * Update personal information
     *
     * @param username
     * @param data
     * @return Task<Void>
     * @pre user needs to be authenticated
     */
    public static Task updateUserInformation(String username, @NonNull Map data) {
        Log.d("updateUserInformation", "Username: " + username);
        return users.document(username).update(data);
    }

    /**
     * Create user-tailored file in /identity for authentication.
     *
     * @param email
     * @param username
     * @param uid
     * @return Task<Void>
     */
    public static Task reserveIdentity(String email, String username, String uid) {
        Log.d("reserveIdentity", "Email: " + email + " Username: " + username
                + " UID: " + uid);
        Map<String, String> identityFile = new HashMap<>();
        identityFile.put("username", username);
        identityFile.put("uid", uid);
        return identity.document(email).set(identityFile);
    }

    /**
     * Delete user-tailored file for authentication.
     *
     * @param email
     * @return Task<Void>
     */
    public static Task deleteIdentity(String email) {
        Log.d("deleteIdentity", "Email: " + email);
        return identity.document(email).delete();
    }

    /**
     * Create new User in db.
     *
     * @param username
     * @return Task<Void>
     */
    public static Task createNewUser(String username) {
        Log.d("reserveIdentity", "Username: " + username);
        // Fill in empty data
        Map<String, String> userFile = new HashMap<>();
        userFile.put("first_name", "");
        userFile.put("last_name", "");
        userFile.put("age", "");
        userFile.put("gender", "");
        userFile.put("phone_number", "");
        userFile.put("profile_description", "");
        userFile.put("tenant_mode", "true");
        return users.document(username).set(userFile);
    }
}