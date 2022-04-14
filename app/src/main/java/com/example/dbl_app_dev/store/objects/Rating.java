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

    public Rating(AccommodationInfo accommodation, User tenant) {
        //this.id = snapshot.getId();
        this.accommodationId = accommodation.getAccommodationId();
        this.ownerUsername = accommodation.getOwnerUsername();
        this.tenantUsername = tenant.getUsername();
        this.ratingLandlord = (long) 0;
        this.ratingTenant = (long) 0;
    }

    public Rating(AccommodationInfo accommodation, User tenant, Long tenantRating) {
        //this.id = snapshot.getId(); // not initialized, can be pushed
        this.accommodationId = accommodation.getAccommodationId();
        this.ownerUsername = accommodation.getOwnerUsername();
        this.tenantUsername = tenant.getUsername();
        this.ratingLandlord = (long) 0;
        this.ratingTenant = tenantRating;
        // bind objects so that we don't have to download them again
        this.tenant = tenant;
        this.accommodation = accommodation;
    }

    public Long getRatingLandlord() {
        return this.ratingLandlord;
    }

    public Long getRatingTenant() {
        return this.ratingTenant;
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

    //TODO finish
    public void pushRating() throws Exception {
        if (this.id == null) {
            this.id = Database.createRatingAccommodation(this.accommodationId, this.tenantUsername
                    , this.ownerUsername, ratingTenant).getId();
        } else {
            Database.updateRatingAccommodation(this.id, this.accommodationId, this.tenantUsername
                    , this.ownerUsername, this.ratingTenant, this.ratingLandlord);
        }
    }
}
