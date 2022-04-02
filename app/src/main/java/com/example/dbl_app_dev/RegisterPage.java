package com.example.dbl_app_dev;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

    // text boxes
    private EditText email;
    private TextView emailWarning;
    private EditText username;
    private TextView usernameWarning;
    private EditText password;
    private TextView passwordWarning;
    private EditText repeatPassword;
    private TextView repeatPasswordWarning;

    // all used validators
    private ViewValidator emailValidator;
    private ViewValidator userValidator;
    private ViewValidator passValidator;
    private ViewValidator repPassValidator;

    // adapter class for TextWatcher
    private abstract class TextWatcherAdapter implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    /**
     * initialise text boxes
     */
    private void init() {
        // get views by id
        email = findViewById(R.id.editEmailBox);
        emailWarning = findViewById(R.id.invalidEmail);
        username = findViewById(R.id.editUsernameBox);
        usernameWarning = findViewById(R.id.invalidUsername);
        password = findViewById(R.id.editPasswordBox);
        passwordWarning = findViewById(R.id.invalidPassword);
        repeatPassword = findViewById(R.id.editRepeatPasswordBox);
        repeatPasswordWarning = findViewById(R.id.invalidRepeatPassword);

        // instantiate validators
        emailValidator = new EmailValidator(email, emailWarning);
        userValidator = new UsernameValidator(username, usernameWarning);
        passValidator = new PasswordValidator(password, passwordWarning);
        repPassValidator = new RepeatPasswordValidator(repeatPassword, password, repeatPasswordWarning);

        // add reactivity listeners for text views
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    emailValidator.validate();
                }
            }
        });

        username.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userValidator.validate();
            }
        });

        password.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passValidator.validate();
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    repPassValidator.validate();
                }
            }
        });

        repeatPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    repPassValidator.validate();
                }
            }
        });
    }

    /**
     * initially sets all warnings to "gone"
     */
    private void makeWarningsInvisible() {
        emailWarning.setVisibility(View.GONE);
        usernameWarning.setVisibility(View.GONE);
        passwordWarning.setVisibility(View.GONE);
        repeatPasswordWarning.setVisibility(View.GONE);
    }

    /**
     * Checks whether sign-up is valid and updates warning messages accordingly
     *
     * @return true iff all text boxes contain valid text, and false otherwise
     * @modifies text view visibility
     */
    private boolean isSignUpValid() {

        // list of all validator objects
        ArrayList<ViewValidator> validators = new ArrayList<>();

        // add all validators to list
        validators.add(emailValidator);
        validators.add(userValidator);
        validators.add(passValidator);
        validators.add(repPassValidator);

        // run validate on all validators
        boolean areValidatorsValid = true;
        for (ViewValidator validator : validators) {
            validator.validate();

            if (!validator.isValid()) {
                areValidatorsValid = false;
            }
        }

        // if all validators are valid, then return true, otherwise return false
        return areValidatorsValid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Log in button leading to LoginPage
        TextView loginTxt = findViewById(R.id.loginTxt);
        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterPage.this,
                        LoginPage.class));
                overridePendingTransition(0, 0);
            }
        });
    }
}