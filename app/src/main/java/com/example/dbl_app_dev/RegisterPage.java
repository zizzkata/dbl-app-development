package com.example.dbl_app_dev;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dbl_app_dev.network_communication.Authentication;
import com.example.dbl_app_dev.store.Store;
import com.example.dbl_app_dev.util.view_validation.validators.EmailValidator;
import com.example.dbl_app_dev.util.view_validation.validators.PasswordValidator;
import com.example.dbl_app_dev.util.view_validation.validators.RepeatPasswordValidator;
import com.example.dbl_app_dev.util.view_validation.validators.UsernameValidator;
import com.example.dbl_app_dev.util.view_validation.validators.ViewValidator;

import java.util.ArrayList;

public class RegisterPage extends AppCompatActivity {
    // text boxes
    private EditText email;
    private TextView emailWarning;
    private EditText username;
    private TextView usernameWarning;
    private EditText password;
    private TextView passwordWarning;
    private EditText repeatPassword;
    private TextView repeatPasswordWarning;
    private TextView loginTxt;
    private TextView signupBtn;

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
        signupBtn = findViewById(R.id.signupBtn);
        loginTxt = findViewById(R.id.loginTxt);

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        init();
        makeWarningsInvisible();

        //Log in button leading to LoginPage

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSignUpValid()) {
                    //return;
                }

                String emailString = email.getText().toString();
                String usernameString = username.getText().toString();
                String passwordString = password.getText().toString();

                //if ()
                if (Authentication.isUsernameUnique("test55")) {
                    return;
                }

                Store.signup(emailString, passwordString, usernameString);



//                startActivity(new Intent(com.example.dbl_app_dev.RegisterPage.this,
//                        LoginPage.class));
            }
        });

        // Login text button
        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeWarningsInvisible();
                startActivity(new Intent(RegisterPage.this,
                        LoginPage.class));
            }
        });
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

    /**
     * initially sets all warnings to "gone"
     */
    private void makeWarningsInvisible() {
        emailWarning.setVisibility(View.GONE);
        usernameWarning.setVisibility(View.GONE);
        passwordWarning.setVisibility(View.GONE);
        repeatPasswordWarning.setVisibility(View.GONE);
    }
}