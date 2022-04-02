package com.example.dbl_app_dev;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

public class LoginPage extends AppCompatActivity {

    // text boxes
    private EditText email;
    private TextView credentialsWarning;
    private EditText password;
    private TextView loginButton;

    /**
     * initialise text boxes
     */
    private void init() {
        //TODO implement warrnings
        email = findViewById(R.id.editUsernameBox);
        password = findViewById(R.id.editPasswordBox);

        credentialsWarning = findViewById(R.id.invalidCredentials);
        credentialsWarning.setVisibility(View.GONE);

        loginButton = findViewById(R.id.logInButton);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Sign Up button leading to RegisterPage
        TextView signUpTxt = findViewById(R.id.signUpTxt);
        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, RegisterPage.class));
                overridePendingTransition(0, 0);
            }
        });

        // Log in button leading to MainNavigationPage
        TextView loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, MainNavigationActivity.class));
                overridePendingTransition(0, 0);
            }
        });
    }

    private void onLogin(Task<AuthResult> result) {
        this.runOnUiThread(() -> {
            startActivity(new Intent(LoginPage.this, RegisterPage.class));
        });
    }

    private void onFail(Task<AuthResult> task) {
        Toast.makeText(this, task.getException().getMessage(),
                Toast.LENGTH_LONG);
    }
}