package com.example.dbl_app_dev.store;


import com.example.dbl_app_dev.store.objects.User;
import com.example.dbl_app_dev.network_communication.Authentication;
import com.google.firebase.auth.FirebaseUser;

public final class Store {
    private Store() throws Exception {
        throw new Exception("Cannot innitializee this class");
    }

    private static User currentUser;


    private static Exception lastExecutedException;

    public static boolean login(String email, String password) {
        FirebaseUser auth;
        try {
            auth = Authentication.firebaseLogin(email, password).getUser();
            currentUser = new User(auth);
        } catch (Exception e) {
            lastExecutedException = e;
            return false;
        }
        return true;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

}
