package com.example.dbl_app_dev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {

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
}