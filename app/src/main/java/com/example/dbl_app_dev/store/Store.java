package com.example.dbl_app_dev.store;

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
    private static ArrayList<Rating> ratingsLandlord = new ArrayList<>();
    private static ArrayList<String> ratedAccommodationsIds = new ArrayList<>();

    private static Query discoveryFilter;

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
        return discoveryAccommodations.remove(0);
    }

    //TODO add filtes
    public static void refreshAccommodations() throws Exception {
        discoveryAccommodations = transformDocuments(
                    Database.getActiveAccommodations(getCurrentUser().getUsername(), MAX_ACCOMMODATIONS_PER_PULL));
    }

    public static User getUserToBeRated()  throws Exception {
        if (ratingsLandlord.size() == 0) {
            //ratingsLandlord = Database.getRatedTenants(currentUser.getUsername());
        }
        return null;
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
        if (ratingsTenant.size() == 0) {
            ratingsTenant = Database.getRatedAccommodations(currentUser.getUsername());
        }
        return ratingsTenant;
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

    public static void killStore() {
        currentUser = null;
        discoveryAccommodations = new ArrayList<>();
        ratingsTenant = new ArrayList<>();
    }
}
