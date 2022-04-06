package com.example.dbl_app_dev.network_communication;

import android.graphics.Bitmap;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class FirebaseQueries {

    private final static long ONE_MEGABYTE = 1024 * 1024;
    /**
     * firebase db
     */
    private static FirebaseFirestore fireStore = FirebaseFirestore
            .getInstance(Authentication.getApp());

    private static FirebaseStorage firebaseStorage = FirebaseStorage
            .getInstance(Authentication.getApp());

    /**
     * db collections
     */
    private static CollectionReference users = fireStore.collection("users");
    private static CollectionReference identity = fireStore.collection("identity");
    private static CollectionReference accommodations = fireStore
            .collection("accommodations");

    /**
     * file collections
     */
    private static StorageReference usersImages = firebaseStorage.getReference("users");
    private static StorageReference panoramicImages = firebaseStorage
            .getReference("accommodations_360");
    private static StorageReference staticImages = firebaseStorage
            .getReference("accommodations_static");

    /**
     * Get batch job from Firestore
     *
     * @return Batch
     */
    public static WriteBatch getBatch() {
        return fireStore.batch();
    }

    /**
     * Returns a transaction encapsulation
     *
     * @param function
     * @param <U>
     * @return Task<TResult>
     */
    public static <U> Task getTransaction(Transaction.Function<U> function) {
        return fireStore.runTransaction(function);
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
        Map<String, Object> userFile = new HashMap<>();
        userFile.put("first_name", "");
        userFile.put("last_name", "");
        userFile.put("age", "");
        userFile.put("gender", "");
        userFile.put("phone_number", "");
        userFile.put("profile_description", "");
        userFile.put("tenant_mode", true);
        userFile.put("smoke", false);
        userFile.put("pets", false);
        return users.document(username).set(userFile);
    }

    /**
     * Push data safely ith a transaction to Firebase database
     * @param docReference
     * @param newData
     * @return
     */
    public static Transaction.Function<Void> pushData(DocumentReference docReference, Map<String
            , Object> newData) {
        return new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(docReference);
                transaction.update(docReference, newData);
                return null;
            }
        };
    }

    public static Task<QuerySnapshot> getAccommodations(int offset) {
        return accommodations.get();
    }

    /**
     * Pull image from Firebase Storage.
     * @param username
     * @return
     */
    public static Task<byte[]> getUserImage(String username) {
        return usersImages.child(username + ".jpg").getBytes(ONE_MEGABYTE);
    }

    /**
     *
     * @param accommodationId
     * @return
     */
    public static Task<byte[]> getPanoramicImage(String accommodationId) {
        return panoramicImages.child(accommodationId + ".jpg").getBytes(ONE_MEGABYTE);
    }

    public static Task<ListResult> getListStaticImages(String accommodationId) {
        return staticImages.child(accommodationId).listAll();
    }

    public static Task<byte[]> getReference(StorageReference reference) {
        return reference.getBytes(ONE_MEGABYTE);
    }
    /**
     *
     * @param accommodationId
     * @param imageBytes
     * @return
     */
    public static UploadTask uploadPanoramicImage(String accommodationId, byte[] imageBytes) {
        return panoramicImages.child(accommodationId + ".jpg").putBytes(imageBytes);
    }

}