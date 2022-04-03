package com.example.dbl_app_dev;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dbl_app_dev.store.Store;
import com.example.dbl_app_dev.util.view_validation.scenarios.NetworkLoginErrorScenario;

public class LoginPage extends AppCompatActivity {

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        init();

        // Sign Up button leading to RegisterPage
        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCredentialsWarning(false);
                startActivity(new Intent(LoginPage.this, RegisterPage.class));
                overridePendingTransition(0, 0);
            }
        });

        // Log in button leading to MainNavigationPage
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCredentialsWarning(false); // hide error message
                login(view);
            }
        });
    }

    private void login(View view) {
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        if (emailString == "" || passwordString == "") {
            setCredentialsWarning("Please fill in all fields.", true);
            return;
        }

        if (Store.login(emailString, passwordString)) {
            startActivity(new Intent(LoginPage.this, MainNavigationActivity.class));
            overridePendingTransition(0, 0);
        } else {
            String err = new NetworkLoginErrorScenario(Store.getLastException()).getError();
            setCredentialsWarning(err, true);
        }
    }

    /** Controls for the warning message field */
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