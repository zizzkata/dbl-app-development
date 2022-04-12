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
import java.util.HashMap;
import java.util.Map;

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
    private DocumentSnapshot snapshot;

    public User(FirebaseUser user) throws Exception {
        this.email = user.getEmail();
        this.username = Database.getUsername(this.email);
        getInformation();
    }
    
    public User(DocumentSnapshot res) {
        this.username = res.getId();
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

    /**
     *  Pull user information from db
     * @throws Exception
     */
    private void getInformation() throws Exception {
        DocumentSnapshot res = Database.getUserInformation(this.username);
        this.snapshot = res;
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


    /** setters */
    public void setEmail(String email) {
        this.email = email;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setTenantMode(boolean tenantMode) {
        this.tenantMode = tenantMode;
    }

    public void setSmokes(boolean smokes) {
        this.smokes = smokes;
    }

    public void setHasPets(boolean hasPets) {
        this.hasPets = hasPets;
    }

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

    /**
     *
     * @throws Exception
     */
    public void updateUser() throws Exception {
        Database.updateUserInformation(this.username, transformUser());
    }

    /**
     * Reset the variables to the last pulled instance
     */
    public void resetToOriginal() {
        this.firstName = (String) snapshot.get("first_name");
        this.lastName = (String) snapshot.get("last_name");
        this.gender = (String) snapshot.get("gender");
        this.phoneNumber = (String) snapshot.get("phone_number");
        this.description = (String) snapshot.get("profile_description");
        this.age = (String) snapshot.get("age");
        this.tenantMode = (Boolean) snapshot.get("tenant_mode");
        this.smokes = (Boolean) snapshot.get("smoke");
        this.hasPets = (Boolean) snapshot.get("pets");
    }

    private Map<String, Object> transformUser() {
        Map<String, Object> transformedData = new HashMap<>();
        transformedData.put("first_name", this.firstName);
        transformedData.put("last_name", this.lastName);
        transformedData.put("age", this.age);
        transformedData.put("gender", this.gender);
        transformedData.put("pets", this.hasPets);
        transformedData.put("phone_number", this.phoneNumber);
        transformedData.put("profile_description", this.description);
        transformedData.put("smoke", this.smokes);
        return transformedData;
    }
}