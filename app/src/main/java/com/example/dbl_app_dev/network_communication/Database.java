package com.example.dbl_app_dev.network_communication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.dbl_app_dev.util.AsyncWrapper;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Database {

    /**
     * Get username of currently authenticated user
     *
     * @param email
     * @pre user is authenticated
     */
    public static String getUsername(String email) throws Exception {
        return (String) Tasks.await((FirebaseQueries.getUsername(email))).get("username");
    }

    /**
     * Get user's information
     *
     * @param username
     * @return
     */
    public static DocumentSnapshot getUserInformation(String username) throws Exception {
        return Tasks.await(FirebaseQueries.getUserInformation(username));
    }

    /**
     * Push data to a file.
     *
     * @param documentReference document to update
     * @param data сdata to push
     * @throws Exception
     */
    public static void pushData(DocumentReference documentReference, Map<String, Object> data)
            throws Exception {
        AsyncWrapper.wrap(FirebaseQueries.getTransaction(
                FirebaseQueries.pushData(documentReference, data)));
    }

    public static Bitmap getUserImage(String username) throws Exception {
        byte[] byteArray = AsyncWrapper.wrap(FirebaseQueries.getUserImage(username));
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static Bitmap getPanoramicImage(String accommId) throws Exception {
        byte[] byteArray = AsyncWrapper.wrap(FirebaseQueries.getPanoramicImage(accommId));
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static ArrayList<byte[]> getStaticImages(String accommId) throws Exception {
        List<StorageReference> listResult = AsyncWrapper.wrap(FirebaseQueries.getListStaticImages(accommId))
                .getItems();
        ArrayList<byte[]> byteImages = new ArrayList<>();
        for (StorageReference sr : listResult) {
            byteImages.add(AsyncWrapper.wrap(FirebaseQueries.getReference(sr)));
        }
        return byteImages;
    }

    public static ArrayList<Bitmap> getStaticImagesBitmaps(String accommId) throws Exception {
        List<StorageReference> listResult = AsyncWrapper.wrap(FirebaseQueries.getListStaticImages(accommId))
                .getItems();
        ArrayList<Bitmap> byteImages = new ArrayList<>();
        for (StorageReference sr : listResult) {
            byte[] arr = AsyncWrapper.wrap(FirebaseQueries.getReference(sr));
            byteImages.add(BitmapFactory.decodeByteArray(arr, 0, arr.length));
        }
        return byteImages;
    }

    public static List<DocumentSnapshot> getAccommodations() throws Exception {
        return Tasks.await(FirebaseQueries.getAccommodations(0)).getDocuments();
    }

    public static List<DocumentSnapshot> getActiveAccommodations(int amount) throws Exception {
        return Tasks.await(FirebaseQueries.getActiveAccommodations(amount).get()).getDocuments();
    }

    public static List<DocumentSnapshot> getActiveAccommodations(DocumentSnapshot lastDoc
            , int amount) throws Exception {
        return Tasks.await(FirebaseQueries.getActiveAccommodations(lastDoc, amount).get()).getDocuments();
    }

    public static List<DocumentSnapshot> getActiveAccommodations(Query filter, int amount)
            throws Exception {
        return Tasks.await(filter.limit(amount).get()).getDocuments();
    }

    public static QuerySnapshot filterQuery(Query filter, int amount) throws Exception {
        return Tasks.await(filter.limit(amount).get());
    }

    public static QuerySnapshot filterQuery(Query filter, DocumentSnapshot ds, int amount)
            throws Exception {
        return Tasks.await(filter.startAfter(ds).limit(amount).get());
    }

    public static void updateUserInformation(String username, Map<String, Object> data)
        throws Exception {
        Tasks.await(FirebaseQueries.updateUserInfo(username, data));
    }

    public static ArrayList<DocumentSnapshot> getLikedAccommodations(String username) throws Exception {
        QuerySnapshot likedAccommodations = Tasks.await(
                FirebaseQueries.getLikedAccommodationsIds(username));
        ArrayList<DocumentSnapshot> accommodations = new ArrayList<>();
        for(DocumentSnapshot rate : likedAccommodations.getDocuments()) {
            //TODO add try/catch
            accommodations.add(Tasks.await(FirebaseQueries.getAccommodation(rate.getId())));
        }
        return accommodations;
    }

    public static void rateAccommodation(String accommodationId, String username,
                                         boolean rating) throws Exception {
        Tasks.await(FirebaseQueries.rateAccommodation(accommodationId, username, rating));
    }
}
