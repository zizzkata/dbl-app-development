package com.example.dbl_app_dev.network_communication;

import com.example.dbl_app_dev.util.AsyncWrapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public abstract class Authentication {

    /**  */
    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    /**
     *
     * @param email
     * @param password
     * @return
     * @throws Exception
     */
    public static AuthResult firebaseLogin(String email, String password) throws Exception {
        return AsyncWrapper.wrap(auth.signInWithEmailAndPassword(email, password));
    }


    public static void firebaseSignup(String email, String password, String username) {
        //TODO
    }

    public static FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public static FirebaseApp getApp() {
        return auth.getApp();
    }
}
