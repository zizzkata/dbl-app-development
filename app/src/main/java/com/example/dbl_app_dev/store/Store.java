package com.example.dbl_app_dev.store;

import com.example.dbl_app_dev.network_communication.Authentication;
import com.example.dbl_app_dev.network_communication.Database;
import com.example.dbl_app_dev.store.objects.AccommodationInfo;
import com.example.dbl_app_dev.store.objects.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that acts as local cache.
 */
public final class Store {

    private static final int MAX_ACCOMMODATIONS_PER_PULL = 5;

    /**
     * States and variables that need to be saved locally
     */
    private static User currentUser;
    private static ArrayList<AccommodationInfo> discoveryAccommodations = new ArrayList<>();
    private static final ArrayList<AccommodationInfo> likedProperties = new ArrayList<>();
    private static final ArrayList<AccommodationInfo> listedProperties = new ArrayList<>();

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
            refreshAccommodations(discoveryFilter);
        } else if (discoveryAccommodations.size() < 2) { // Pull more data
            pullMoreAccommodations(discoveryAccommodations.get(0).getSnapshot());
        }
        if (discoveryAccommodations.size() != 0) {
            return discoveryAccommodations.remove(0);
        }
        return null;
    }

    public static void refreshAccommodations(Query query) throws Exception {
        ArrayList<AccommodationInfo> newData;
        if (query == null) {
            newData = transformDocuments(
                    Database.getActiveAccommodations(MAX_ACCOMMODATIONS_PER_PULL));
            discoveryFilter = null;
        } else {
            newData = transformDocuments(
                    Database.filterQuery(query, MAX_ACCOMMODATIONS_PER_PULL).getDocuments());
            discoveryFilter = query;
        }
        discoveryAccommodations = newData; // let garbage collector take care
    }

    public static void pullMoreAccommodations(DocumentSnapshot lastAccommodation) throws Exception {
        if (discoveryFilter == null) {
            ArrayList<AccommodationInfo> newData = transformDocuments(
                    Database.getActiveAccommodations(lastAccommodation
                            , MAX_ACCOMMODATIONS_PER_PULL));
        } else {
            ArrayList<AccommodationInfo> newData = transformDocuments(
                    Database.filterQuery(discoveryFilter
                            , lastAccommodation
                            , MAX_ACCOMMODATIONS_PER_PULL).getDocuments());
        }

    }

    private static ArrayList<AccommodationInfo> transformDocuments(List<DocumentSnapshot> documents) {
        ArrayList<AccommodationInfo> returnArray = new ArrayList<>();
        for (DocumentSnapshot ds : documents) {
            returnArray.add(new AccommodationInfo(ds));
        }
        return returnArray;
    }


    public static void killStore() {
        currentUser = null;
        discoveryAccommodations = new ArrayList<>();
    }

}
