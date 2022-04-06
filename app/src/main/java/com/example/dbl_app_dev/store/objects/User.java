package com.example.dbl_app_dev.store.objects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.dbl_app_dev.network_communication.Database;
import com.google.common.collect.BiMap;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.InputStream;

public class User {

    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String gender;
    private String description;
    private String phoneNumber;
    private String age;
    private Bitmap profilePic;
    private boolean tenantMode;
    private boolean smokes;
    private boolean hasPets;

    public User(FirebaseUser user) throws Exception {
        this.email = user.getEmail();
        this.username = Database.getUsername(this.email);
        getInformation();
    }

    private void getInformation() throws Exception {
        DocumentSnapshot res = Database.getUserInformation(this.username);
        this.firstName = (String) res.get("first_name");
        this.lastName = (String) res.get("last_name");
        this.gender = (String) res.get("gender");
        this.phoneNumber = (String) res.get("phone_number");
        this.description = (String) res.get("profile_description");
        this.age = (String) res.get("age");
        this.tenantMode = (Boolean) res.get("tenant_mode");
        this.smokes = (Boolean) res.get("smoke");
        this.hasPets = (Boolean) res.get("pets");
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() { return lastName; }

    public String getUsername() { return username; }

    public String getEmail() { return email; }

    public String getAge() { return age; }

    public String getGender() { return gender; }

    public String getPhoneNumber() { return phoneNumber; }

    public String getDescription() { return description; }

    public boolean isTenantMode() { return tenantMode; }

    public boolean hasPets() { return hasPets; }

    public boolean smokes() { return smokes; }

    /**
     *
     * @return
     */
    public Bitmap getProfilePic()  {
        if (profilePic == null) {
            try {
                profilePic = Database.getUserImage(username);
            } catch(Exception e) {
                // Not found
                Log.d("getProfilePic", e.getMessage());
            }
        }
        return profilePic;
    }
}