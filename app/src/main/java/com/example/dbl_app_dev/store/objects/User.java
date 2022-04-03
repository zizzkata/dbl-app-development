package com.example.dbl_app_dev.store.objects;

import android.util.Log;
import com.example.dbl_app_dev.network_communication.Database;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class User {
    private com.google.firebase.firestore.auth.User auth;

    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String gender;
    private String description;
    private String phoneNumber;
    private String age;
    private boolean tenantMode;

    public User(FirebaseUser user) throws Exception {
        this.email = user.getEmail();
        this.username = Database.getUsername(this.email);

        DocumentSnapshot res = Database.getUserInformation(this.username);
        this.firstName = (String) res.get("first_name");
        this.lastName = (String) res.get("last_name");
        this.gender = (String) res.get("gender");
        this.phoneNumber = (String) res.get("phone_number");
        this.description = (String) res.get("profile_description");
        this.age = (String) res.get("age");
        // Log.i("User", this.firstName == null ? "null" : this.firstName); // TEST
        // pull image
        // TODO

    }

    public String getFirstName() {
        return firstName;
    }
}
