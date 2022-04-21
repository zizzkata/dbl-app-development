package com.example.dbl_app_dev;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.dbl_app_dev.network_communication.Authentication;
import com.example.dbl_app_dev.store.Store;
import com.example.dbl_app_dev.util.AsyncWrapper;
import com.example.dbl_app_dev.util.view_validation.constants.Exceptions;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {

    // text boxes
    private EditText email;
    private TextView credentialsWarning;
    private EditText password;

    private TextView loginBtn;
    private TextView signUpTxt;

    /**
     * initialise text boxes
     */
    private void init() {
        // Text fields
        email = findViewById(R.id.editUsernameBox);
        password = findViewById(R.id.editPasswordBox);

        // Warnings
        credentialsWarning = findViewById(R.id.invalidCredentials);
        credentialsWarning.setVisibility(View.GONE);

        // Buttons
        loginBtn = findViewById(R.id.loginBtn);
        signUpTxt = findViewById(R.id.signUpTxt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_login_page);

        init();

        // Sign Up button leading to RegisterActivity
        signUpTxt.setOnClickListener(view -> {
            setCredentialsWarning(false);
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });

        // Log in button leading to MainNavigationPage
        loginBtn.setOnClickListener(view -> {
            setCredentialsWarning(false); // hide error message
            String emailString = email.getText().toString();
            String passwordString = password.getText().toString();

            loginBtn.setEnabled(false);

            if (emailString.equals("") || passwordString.equals("")) {
                setCredentialsWarning("* Please fill in all fields.", true);
                return;
            }

            AsyncWrapper.wrap(() -> login(emailString, passwordString));
        });
    }

    private void login(String email, String password) {
        try {
            AuthResult res = Tasks.await(Authentication.firebaseLogin(email, password));
            runOnUiThread(() -> {
                startActivity(new Intent(LoginActivity.this, MainNavigationActivity.class));
                overridePendingTransition(0, 0);
                loginBtn.setEnabled(true);
                finish();
            });
            Store.getCurrentUser(); // don't get the parameter
        } catch (

        Exception e) {
            Log.e("ERR", e.getMessage());
            // String warning =
            // Exceptions.getWarning(Store.getLastException().getMessage());
            runOnUiThread(() -> {
                setCredentialsWarning(Exceptions.getWarning(e.getMessage()), true);
                loginBtn.setEnabled(true);
            });
        }
    }

    /**
     * Controls for the warning message field
     */
    private void setCredentialsWarning(String message, boolean visible) {
        credentialsWarning.setText(message);
        credentialsWarning.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setCredentialsWarning(boolean visible) {
        credentialsWarning.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setCredentialsWarning(String message) {
        credentialsWarning.setText(message);
    }
}