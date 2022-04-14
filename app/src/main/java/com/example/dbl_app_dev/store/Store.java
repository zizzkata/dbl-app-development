package com.example.dbl_app_dev.store;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.dbl_app_dev.network_communication.Authentication;
import com.example.dbl_app_dev.network_communication.Database;
import com.example.dbl_app_dev.store.objects.AccommodationInfo;
import com.example.dbl_app_dev.store.objects.Rating;
import com.example.dbl_app_dev.store.objects.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that acts as local cache.
 */
public final class Store {

    private static final int MAX_ACCOMMODATIONS_PER_PULL = 3;

    /**
     * States and variables that need to be saved locally
     */
    private static User currentUser;
    private static ArrayList<AccommodationInfo> discoveryAccommodations = new ArrayList<>();
    private static ArrayList<AccommodationInfo> listedProperties = new ArrayList<>();
    private static ArrayList<Rating> ratingsTenant = new ArrayList<>();
    private static ArrayList<User> ratingsLandlord = new ArrayList<>();
    private static ArrayList<String> ratedAccommodationsIds = new ArrayList<>();

    private static Query discoveryFilter; // not used for current version
    private static boolean filters = false;
    private static Long min = 300L;
    private static Long max = 600L;

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
     * @return
     */
    public static AccommodationInfo getNextAccommodation() throws Exception {
        // Refresh
        if (discoveryAccommodations.size() == 0) {
            refreshAccommodations();
        }
        Log.d("discoveryAccomm.size", String.valueOf(discoveryAccommodations.size()));
        return discoveryAccommodations.remove(0);
    }

    // Implemented OK
    public static void refreshAccommodations() throws Exception {
        if (filters) {
            discoveryAccommodations = transformDocuments(
              Database.getFilteredPriceActiveAccommodations(getCurrentUser().getUsername()
                ,MAX_ACCOMMODATIONS_PER_PULL, min, max)
            );
        } else {
            discoveryAccommodations = transformDocuments(
                    Database.getActiveAccommodations(getCurrentUser().getUsername(), MAX_ACCOMMODATIONS_PER_PULL));
        }
    }

    public static User getUserToBeRated()  throws Exception {
        if (ratingsLandlord.size() == 0) {
            refreshRatedUsers();
        }
        return ratingsLandlord.remove(0);
    }

    public static void refreshRatedUsers() throws Exception {
        ratingsLandlord = Database.getRatedTenants(getCurrentUser().getUsername());
    }

    @Deprecated
    public static void pullMoreAccommodations(DocumentSnapshot lastAccommodation) throws Exception {
        if (discoveryFilter == null) {
            ArrayList<AccommodationInfo> newData = transformDocuments(
                    Database.getActiveAccommodations(lastAccommodation, MAX_ACCOMMODATIONS_PER_PULL));
        } else {
            ArrayList<AccommodationInfo> newData = transformDocuments(
                    Database.filterQuery(discoveryFilter, lastAccommodation, MAX_ACCOMMODATIONS_PER_PULL)
                            .getDocuments());
        }
    }

    @Deprecated
    public static void pullMoreAccommodations() throws Exception {
        discoveryAccommodations = transformDocuments(
                    Database.getActiveAccommodations(currentUser.getUsername()
                            , MAX_ACCOMMODATIONS_PER_PULL));
    }

    @Deprecated
    public static ArrayList<AccommodationInfo> getCurrentUserLikedAccommodations()
            throws Exception {
        return null;
    }

    public static ArrayList<Rating> getRatingsTenant() throws Exception {
        return Database.getRatedAccommodations(currentUser.getUsername());
    }

    /**
     *
     * @param accommodation
     * @param impression
     * @throws Exception
     */
    public static void rateAccommodation(AccommodationInfo accommodation, Long impression)
            throws Exception {
        if (impression > 1 || impression < -1)
            throw new Exception("Invalid impression of the rating given.");

        Rating rating = new Rating(accommodation, currentUser, impression);
        rating.pushRating();
        ratingsTenant.add(rating);
    }

    private static ArrayList<AccommodationInfo> transformDocuments(List<DocumentSnapshot> documents) {
        ArrayList<AccommodationInfo> returnArray = new ArrayList<>();
        for (DocumentSnapshot ds : documents) {
            returnArray.add(new AccommodationInfo(ds));
        }
        return returnArray;
    }

//    private static ArrayList<User> extractDiscoveryUsers() throws Exception {
//        ArrayList<Rating> rt = getRatingsTenant();
//        ArrayList<User> usersExtracted = new ArrayList<>();
//        for (Rating rating : rt) {
//            if (rating.getRatingLandlord() != 0)
//                discoveryUsers.add(rating.getTenant());
//        }
//        return usersExtracted;
//    }

    private static User extractDiscoveryUser(Rating rating) throws Exception {
        if (rating.getRatingLandlord() != 0)
            return rating.getTenant();
        return null;
    }

    // Works for price only
    public static void resetFilters() {
        discoveryFilter = null;
        discoveryAccommodations.clear();
    }

    // Works only for price
    public static void setFilters(Long minValue, Long maxValue) {
        filters = true;
        min = minValue;
        max = maxValue;
        discoveryAccommodations.clear();
    }

    public static void killStore() {
        currentUser = null;
        filters = false;
        discoveryAccommodations = new ArrayList<>();
        ratingsTenant = new ArrayList<>();
    }
}
