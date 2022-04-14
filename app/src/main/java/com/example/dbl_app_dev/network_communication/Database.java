package com.example.dbl_app_dev.network_communication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.dbl_app_dev.store.objects.AccommodationInfo;
import com.example.dbl_app_dev.store.objects.Rating;
import com.example.dbl_app_dev.store.objects.User;
import com.example.dbl_app_dev.util.AsyncWrapper;
import com.example.dbl_app_dev.util.Tools;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.security.InvalidParameterException;
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



    /**
     *  IMAGE MANIPULATION
     */



    public static Bitmap getUserImage(String username) throws Exception {
        byte[] byteArray = AsyncWrapper.wrap(FirebaseQueries.getUserImage(username));
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static Bitmap getPanoramicImage(String accommId) throws Exception {
        byte[] byteArray = AsyncWrapper.wrap(FirebaseQueries.getPanoramicImage(accommId));
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static void uploadProfilePic(String username, Bitmap image) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        Tasks.await(FirebaseQueries.uploadUserProfile(username, stream.toByteArray()));
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

    /**
     *
     * @param accommodationId
     * @param images
     * @throws Exception
     * @throws InvalidParameterException
     */
    public static void uploadStaticImages(String accommodationId
            , ArrayList<Bitmap> images) throws Exception, InvalidParameterException {
        if (images == null || images.size() == 0)
            throw new InvalidParameterException("No empty images allowed");

        ArrayList<byte[]> byteImages = new ArrayList<>();
        for (Bitmap imgBitmap : images) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            byteImages.add(stream.toByteArray());
        }
        ArrayList<UploadTask> tasks = FirebaseQueries
                .uploadStaticImagesAccommodation(accommodationId, byteImages);
        for(UploadTask task : tasks) {
            Tasks.await(task);
        }
    }

    public static void uploadPanoramicImage(String accommodationId, Bitmap image) throws Exception
        ,InvalidParameterException {
        if (image == null)
            throw new InvalidParameterException("Image is null");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        Tasks.await(FirebaseQueries.uploadPanoramicImageAccommodation(accommodationId
                , stream.toByteArray()));
    }


    /**
     * ACCOMMODATION
     */


    public static List<DocumentSnapshot> getAccommodations() throws Exception {
        return Tasks.await(FirebaseQueries.getAccommodations(0)).getDocuments();
    }

    public static List<DocumentSnapshot> getActiveAccommodations(int amount) throws Exception {
        return Tasks.await(FirebaseQueries.getActiveAccommodations(amount).get()).getDocuments();
    }

    public static List<DocumentSnapshot> getActiveAccommodations(String username, int amount)
            throws Exception {
        QuerySnapshot viewAccommodations = Tasks.await(
                FirebaseQueries.getRatedAccommodations(username));
        ArrayList<String> accommodationIds =
                Tools.getListParameterFromCollection("accommodation_id"
                        , viewAccommodations.getDocuments());
        List<DocumentSnapshot> activeAccommodations = new ArrayList<>();
        if (accommodationIds.size() > 0) {
            activeAccommodations  = Tasks.await(FirebaseQueries.getActiveAccommodations(accommodationIds
                    , username, amount).get()).getDocuments();
        } else {
            activeAccommodations = Tasks.await(FirebaseQueries.getActiveAccommodations(username, amount).get())
                    .getDocuments();
        }
        activeAccommodations.removeIf(accomm -> accomm.get("owner_username") == username);
        return activeAccommodations;
    }



    public static AccommodationInfo getAccommodation(String accommodationId) throws Exception {
        return new AccommodationInfo(
                Tasks.await(FirebaseQueries.getAccommodation(accommodationId)));
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

    @Deprecated
    public static ArrayList<DocumentSnapshot> getLikedAccommodations(String username) throws Exception {
        QuerySnapshot likedAccommodations = Tasks.await(
                FirebaseQueries.getLikedAccommodations(username));
        ArrayList<DocumentSnapshot> accommodations = new ArrayList<>();
        for(DocumentSnapshot rate : likedAccommodations.getDocuments()) {
            //TODO add try/catch
            accommodations.add(Tasks.await(FirebaseQueries.getAccommodation(
                    (String) rate.get("accommodation_id"))));
        }
        return accommodations;
    }

    public static void createRatingAccommodation(String accommodationId, String username
            , String ownerUsername, Long rating) throws Exception {
        Tasks.await(FirebaseQueries.createRatingOnAccommodation(accommodationId
                , username, ownerUsername, rating));
    }

    public static void deleteRatingAccommodation(String accommodationId, String tenantUsername)
            throws Exception {
        DocumentSnapshot reference = Tasks.await(
                FirebaseQueries.getOneRatingOnAccommodation(accommodationId, tenantUsername)
                        .get()).getDocuments().get(0);
        Tasks.await(FirebaseQueries.deleteRatingOnAccommodation(reference.getId()));
    }

    public static DocumentReference createAccommodation(Map<String, Object> data) throws Exception {
        return Tasks.await(FirebaseQueries.createAccommodationListing(data));
    }

    public static void updateAccommodation(String accommodationId, Map<String, Object> newData) throws Exception {
        Tasks.await(FirebaseQueries.updateAccommodationListing(accommodationId, newData));
    }

    public static ArrayList<User> getRatedTenants(String ownerUsername) throws Exception {
        QuerySnapshot active = Tasks.await(
                FirebaseQueries.getActiveAccommodationsOwner(ownerUsername));
        ArrayList<User> users = new ArrayList<>();
        for (DocumentSnapshot activeAcc : active.getDocuments()) {
            try {
                QuerySnapshot accommodations = Tasks.await(
                        FirebaseQueries.getRatingsByAccommodationId(activeAcc.getId()));
                for (DocumentSnapshot listing : accommodations.getDocuments()) {
                    DocumentSnapshot user = Tasks.await(FirebaseQueries.getUserInformation(
                            (String) listing.get("tenantUsername")));
                    users.add(new User(user));
                }
            } catch (Exception e) {
                Log.e("getRatedTenants: invalid listing",e.getMessage());
                // IGNORE
            }
        }
        return users;
    }

    public static ArrayList<AccommodationInfo> getActiveAccommodationsByOwner(String ownerUsername)
            throws Exception {
        QuerySnapshot active = Tasks.await(
                FirebaseQueries.getActiveAccommodationsOwner(ownerUsername));
        ArrayList<AccommodationInfo> activeAccommodations = new ArrayList<>();
        for (DocumentSnapshot ds : active.getDocuments()) {
            activeAccommodations.add(new AccommodationInfo(ds));
        }
        return activeAccommodations;
    }

    public static ArrayList<Rating> getRatedAccommodations(String username) throws Exception {
        ArrayList<Rating> ratings = new ArrayList<>();
        QuerySnapshot snapshotRatings = Tasks.await(
                FirebaseQueries.getRatedAccommodations(username));
        for (DocumentSnapshot ds : snapshotRatings.getDocuments()) {
            // Only add active accommodations
            Rating r = new Rating(ds);
            if (r.getAccommodation().getActive()) {
            ratings.add(r);
            }
        }

        return ratings;
    }
}
