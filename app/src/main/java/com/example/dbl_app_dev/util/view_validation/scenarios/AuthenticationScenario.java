package com.example.dbl_app_dev.util.view_validation.scenarios;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.util.AsyncWrapper;
import com.example.dbl_app_dev.util.view_validation.constants.Exceptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public final class AuthenticationScenario extends TextViewScenario {
    private TextView password;

    public AuthenticationScenario(TextView password, String warning) {
        super(warning);
        this.password = password;
    }

    @Override
    public boolean isValid(View view) {
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();
            String password = textViewToString((TextView) this.password);
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, password);

            // user gets authenticated with current email and password given by validator
            AsyncWrapper.wrap(user.reauthenticate(credential));
            return true;
        } catch (Exception e) {
            super.warningText = Exceptions.getWarning(e.getMessage());
            Log.e("isUsernameUnique", e.getMessage());
        }
        return false;
    }
}
