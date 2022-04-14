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
import com.google.firebase.firestore.Query;
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
    private static CollectionReference ratedAccommodations = fireStore
            .collection("rated_accommodations");
    private static CollectionReference usersAccommodations = fireStore
            .collection("rated_accommodations_users");

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
     *
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

    public static Task<DocumentSnapshot> getAccommodation(String id) {
        return accommodations.document(id).get();
    }

    /**
     * Pull image from Firebase Storage.
     *
     * @param username
     * @return
     */
    public static Task<byte[]> getUserImage(String username) {
        return usersImages.child(username + ".jpg").getBytes(ONE_MEGABYTE);
    }

    public static UploadTask uploadUserProfile(String username, byte[] image) {
        return usersImages.child(username + ".jpg").putBytes(image);
    }

    /**
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
     * @param accommodationId
     * @param imageBytes
     * @return
     */
    public static UploadTask uploadPanoramicImage(String accommodationId, byte[] imageBytes) {
        return panoramicImages.child(accommodationId + ".jpg").putBytes(imageBytes);
    }

    /**
     * @param lastDocument
     * @param amount
     * @return
     */
    public static Query getActiveAccommodations(DocumentSnapshot lastDocument, int amount) {
        return accommodations.startAfter(lastDocument).whereEqualTo("active", true)
                .limit(amount);
    }

    /**
     *
     * @param lastDocument
     * @return
     */
    public static Query getActiveAccommodations(DocumentSnapshot lastDocument) {
        return accommodations.startAfter(lastDocument).whereEqualTo("active", true);
    }

    ///public static Query

    /**
     *
     * @return
     */
    public static Query getActiveAccommodations() {
        return accommodations.whereEqualTo("active", true);
    }

    /**
     *
     * @param amount
     * @return
     */
    public static Query getActiveAccommodations(int amount) {
        return accommodations.whereEqualTo("active", true).limit(amount);
    }

    public static Query getActiveAccommodations(ArrayList<String> exclude, String ownerUsername
            , int amount) {
        return accommodations.whereEqualTo("active", true)
               //.whereNotEqualTo("owner_username", ownerUsername) // cannot do multiple inequality queries
                .whereNotIn("__name__", exclude)
                .limit(amount);
    }

    public static Query getActiveAccommodationsWithinPriceMargin(ArrayList<String> exclude
            , String ownerUsername, int amount, Long min, Long max) {
        return accommodations.whereEqualTo("active", true)
                .whereGreaterThanOrEqualTo("price", min)
                .whereLessThanOrEqualTo("price", max)
                //.whereNotEqualTo("owner_username", ownerUsername) // cannot do multiple inequality queries
                .whereNotIn("__name__", exclude)
                .limit(amount);
    }

    public static Query getActiveAccommodations(String ownerUsername, int amount) {
        return accommodations.whereEqualTo("active", true)
                .whereNotEqualTo("owner_username", ownerUsername)
                .limit(amount);
    }

    public static Query getActiveAccommodationsWithinPriceMargin(String ownerUsername
            , int amount, Long min, Long max) {
        return accommodations.whereEqualTo("active", true)
                .whereNotEqualTo("owner_username", ownerUsername)
                .whereGreaterThanOrEqualTo("price", min)
                .whereLessThanOrEqualTo("price", max)
                .limit(amount);
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static Query filterByPrice(int min, int max) {
        return getActiveAccommodations()
                .whereGreaterThanOrEqualTo("price", min)
                .whereLessThanOrEqualTo("price", max);
    }

    /**
     *
     * @param lastDoc
     * @param min
     * @param max
     * @return
     */
    public static Query filterByPrice(DocumentSnapshot lastDoc, int min, int max) {
        return getActiveAccommodations(lastDoc)
                .whereGreaterThanOrEqualTo("price", min)
                .whereLessThanOrEqualTo("price", max);
    }

    /**
     *
     * @param username
     * @param data
     * @return
     */
    public static Task<Void> updateUserInfo(String username, Map<String, Object> data) {
        return users.document(username).update(data);
    }

    /**
     *
     * @param username
     * @return
     */
    public static Task<QuerySnapshot> getLikedAccommodations(String username) {
        return ratedAccommodations.whereEqualTo("rating_tenant", 1)
                .whereEqualTo("tenant_username", username)
                .get();
    }

    /**
     *
     * @param username
     * @return
     */
    public static Task<QuerySnapshot> getRatedAccommodations(String username) {
        return ratedAccommodations.whereEqualTo("tenant_username", username)
                .get();
    }

    /**
     *
     * @param accommodationId
     * @param tenantId
     * @param ownerId
     * @param rate
     * @return
     */
    public static Task<Void> createRatingOnAccommodation(String accommodationId
            , String tenantId, String ownerId, Long rate) {
        Map<String, Object> rating = new HashMap<>();
        rating.put("accommodation_id", accommodationId);
        rating.put("tenant_username", tenantId);
        rating.put("owner_username", ownerId);
        rating.put("rating_landlord", 0);
        rating.put("rating_tenant", rate);
        return ratedAccommodations.document().set(rating);
    }

    /**
     *
     * @param accommodationId
     * @param tenantId
     * @return
     */
    public static Query getOneRatingOnAccommodation(String accommodationId, String tenantId) {
        return ratedAccommodations
                .whereEqualTo("accommodation_id", accommodationId)
                .whereEqualTo("tenant_username", tenantId)
                .limit(1);
    }

    /**
     *
     * @param documentId
     * @return
     */
    public static Task<Void> deleteRatingOnAccommodation(String documentId) {
        return ratedAccommodations.document(documentId).delete();
    }

    /**
     *
     * @param accommodationId
     * @param newData
     * @return
     */
    public static Task<Void> updateAccommodationListing(String accommodationId
            , Map<String, Object> newData) {
        return accommodations.document(accommodationId).update(newData);
    }

    /**
     *
     * @param newData
     * @return
     */
    public static Task<DocumentReference> createAccommodationListing(Map<String, Object> newData) {
        return accommodations.add(newData);
    }

    /**
     *
     * @param ownerUsername
     * @return
     */
    public static Task<QuerySnapshot> getTenants(String ownerUsername) {
        return ratedAccommodations.whereEqualTo("owner_username", ownerUsername).get();
    }

    /**
     *
     * @param ownerUsername
     * @return
     */
    public static Task<QuerySnapshot> getActiveAccommodationsOwner(String ownerUsername) {
        return accommodations.whereEqualTo("owner_username", ownerUsername)
                .whereEqualTo("active", true)
                .get();
    }

    /**
     *
     * @param accommodationId
     * @return
     */
    public static Task<QuerySnapshot> getRatingsByAccommodationId(String accommodationId) {
        return ratedAccommodations.whereEqualTo("accommodation_id", accommodationId).get();
    }

    public static ArrayList<UploadTask> uploadStaticImagesAccommodation(String accommodationId
            , ArrayList<byte[]> images) {
        ArrayList<UploadTask> task = new ArrayList<>();
        int i = 1;
        for (byte[] image : images) {
            task.add(staticImages.child(accommodationId + "/" + i + ".jpg").putBytes(image));
            i++;
        }
        return task;
    }

    public static UploadTask uploadPanoramicImageAccommodation(String accommodationId, byte[] image) {
        return panoramicImages.child(accommodationId + ".jpg").putBytes(image);
    }
}