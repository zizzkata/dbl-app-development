package com.example.dbl_app_dev.store.objects;

import com.example.dbl_app_dev.network_communication.Database;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * A class representation of a rating (liked accommodation)
 * This class holds the relation between the rating of the user, landlord and accommodation.
 */
public class Rating {

    private String id;
    private String accommodationId;
    private String ownerUsername;
    private String tenantUsername;
    private Long ratingLandlord;
    private Long ratingTenant;
    private DocumentSnapshot snapshot;

    private User tenant;
    private User owner;
    private AccommodationInfo accommodation;

    public Rating(DocumentSnapshot snapshot) {
        this.id = snapshot.getId();
        this.accommodationId = (String) snapshot.get("accommodation_id");
        this.ownerUsername = (String) snapshot.get("owner_username");
        this.tenantUsername = (String) snapshot.get("tenant_username");
        this.ratingLandlord = (Long) snapshot.get("rating_landlord");
        this.ratingTenant = (Long) snapshot.get("rating_tenant");
        this.snapshot = snapshot; // save snapshot just in case
    }

    public User getTenant() throws Exception {
        if (tenant == null) {
            tenant = new User(Database.getUserInformation(this.tenantUsername));
        }
        return tenant;
    }

    public User getOwner() throws Exception {
        if (owner == null) {
            owner = new User(Database.getUserInformation(this.tenantUsername));
        }
        return owner;
    }

    public AccommodationInfo getAccommodation() throws Exception {
        if (accommodation == null) {
            accommodation = Database.getAccommodation(this.accommodationId);
        }
        return accommodation;
    }
}
