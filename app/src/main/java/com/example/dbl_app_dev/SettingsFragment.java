package com.example.dbl_app_dev;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.dbl_app_dev.store.Store;
import com.example.dbl_app_dev.store.objects.User;
import com.example.dbl_app_dev.util.AsyncWrapper;
import com.example.dbl_app_dev.util.Tools;
import com.example.dbl_app_dev.util.adapters.TextWatcherAdapter;
import com.example.dbl_app_dev.util.view_validation.validators.PasswordValidator;
import com.example.dbl_app_dev.util.view_validation.validators.RepeatPasswordValidator;
import com.example.dbl_app_dev.util.view_validation.validators.ViewValidator;
import com.example.dbl_app_dev.util.view_validation.validators.currPasswordValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.dbl_app_dev.store.Store;
import com.example.dbl_app_dev.store.objects.User;
import com.example.dbl_app_dev.util.AsyncWrapper;
import com.example.dbl_app_dev.util.Tools;
import com.example.dbl_app_dev.util.adapters.TextWatcherAdapter;
import com.example.dbl_app_dev.util.view_validation.validators.PasswordValidator;
import com.example.dbl_app_dev.util.view_validation.validators.RepeatPasswordValidator;
import com.example.dbl_app_dev.util.view_validation.validators.ViewValidator;
import com.example.dbl_app_dev.util.view_validation.validators.currPasswordValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // text boxes
    private EditText email;
    private TextView emailWarning;
    private EditText username;
    private TextView usernameWarning;
    private EditText currentPassword;
    private TextView currentPasswordWarning;
    private EditText password;
    private TextView passwordWarning;
    private EditText repeatPassword;
    private TextView repeatPasswordWarning;
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private EditText description;
    private CheckBox smokes;
    private CheckBox hasPets;
    private ImageView profilePic;

    // all used validators
    private ViewValidator emailValidator;
    private ViewValidator userValidator;
    private ViewValidator currPassValidator;
    private ViewValidator passValidator;
    private ViewValidator repPassValidator;

    public SettingsFragment() {
        // Required empty public constructor
    }

    private void init() {
        // get views by id
        email = getView().findViewById(R.id.editTextTextEmailAddress);
        //email.setEnabled(false);
//        emailWarning = getView().findViewById(R.id.invalidEmail);
        username = getView().findViewById(R.id.usernameBox);
        //username.setEnabled(false);
//        usernameWarning = getView().findViewById(R.id.invalidUsername);
        currentPassword = getView().findViewById(R.id.currentPasswordBox);
        currentPasswordWarning = getView().findViewById(R.id.invalidCurrentPassword);
        password = getView().findViewById(R.id.newPasswordBox);
        passwordWarning = getView().findViewById(R.id.invalidPassword);
        repeatPassword = getView().findViewById(R.id.ConfirmNewPasswordBox);
        repeatPasswordWarning = getView().findViewById(R.id.invalidRepeatPassword);

        firstName = getView().findViewById(R.id.firstNameBox);
        lastName = getView().findViewById(R.id.lastNameBox);
        profilePic = getView().findViewById(R.id.imageView);
        phoneNumber = getView().findViewById(R.id.changePhoneBox);
        description = getView().findViewById(R.id.descriptionBox);
        smokes = getView().findViewById(R.id.smokingCheckBox);
        hasPets = getView().findViewById(R.id.havePetsCheckBox);

        // instantiate validators
//        emailValidator = new EmailValidator(email, emailWarning);
//        userValidator = new UsernameValidator(username, usernameWarning);
        currPassValidator = new currPasswordValidator(currentPassword, currentPasswordWarning);
        passValidator = new PasswordValidator(password, passwordWarning);
        repPassValidator = new RepeatPasswordValidator(repeatPassword, password, repeatPasswordWarning);

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

    private void makeWarningsInvisible() {
//        emailWarning.setVisibility(View.GONE);
//        usernameWarning.setVisibility(View.GONE);
        currentPasswordWarning.setVisibility(View.GONE);
        passwordWarning.setVisibility(View.GONE);
        repeatPasswordWarning.setVisibility(View.GONE);
    }

    private boolean areChangesValid() {
        // list of all validator objects
        ArrayList<ViewValidator> validators = new ArrayList<>();

        // add all validators to list
        // SAME FOR EMAIL
        //validators.add(emailValidator);
        // IMPORTANT CANNOT CHANGE USERNAME
        //validators.add(new UsernameUniquenessValidator(username, usernameWarning));

        if (currentPassword.getText().toString().length() > 0) {
            validators.add(currPassValidator);
        }

        if (password.getText().toString().length() > 0) {
            validators.add(passValidator);
        }

        if (repeatPassword.getText().toString().length() > 0) {
            validators.add(repPassValidator);
        }

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

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);
        ((MainNavigationActivity) getActivity()).modeSwitchLogic(root.findViewById(R.id.modeSwitch));

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        makeWarningsInvisible();
        AsyncWrapper.wrap(() -> {
            User user;
            try {
                user = Store.getCurrentUser();
                getActivity().runOnUiThread(() -> bindUserData(user));
            } catch (Exception e) {
                Log.e("OPS", "OPS");
                e.printStackTrace();
                // TODO fail on username
                return;
            }

            Bitmap profilePic = user.getProfilePic();
            try {
                if (profilePic == null) {
                    InputStream stream = getContext()
                            .getAssets().open("default-user-image.png");
                    profilePic = BitmapFactory.decodeStream(stream);
                }
                Bitmap finalProfilePic = profilePic; // necessary for java lint to run ;/
                getActivity().runOnUiThread(() -> bindUserImage(finalProfilePic));
            } catch (Exception e) { // never happens
                Log.e("FATAL", e.getMessage());
                e.printStackTrace();
            }
        });

        TextView addImage = getView().findViewById(R.id.addImageBtn);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        // Sign Up button leading to RegisterPage
        TextView logOutBtn = getView().findViewById(R.id.logoutBtn);
        logOutBtn.setOnClickListener(view1 -> {
            ((MainNavigationActivity) getActivity()).logoutDialog();
        });

        // Save button used to update current user's personal info and password
        TextView saveButton = getView().findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!areChangesValid()) {
                    Context context = getActivity().getApplicationContext();
                    String text = "Not Saved";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    return;
                }

                AsyncWrapper.wrap(() -> {
                    try {
                        updateUserPassword();
                        setNewInformation();
                    } catch (Exception e) {
                        // ERR show
                        Log.e("updateUserInformation", e.getMessage());
                        return;
                    }
                });

                Toast.makeText(getActivity().getApplicationContext()
                        , "Saved", Toast.LENGTH_LONG);
            }
        });
    }

    private void bindUserData(User user) {
        email.setText(user.getEmail());
        username.setText(user.getUsername());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        phoneNumber.setText(user.getPhoneNumber());
        description.setText(user.getDescription());
        smokes.setChecked(user.smokes());
        hasPets.setChecked(user.hasPets());
        // TODO save tenant mode
    }

    private void bindUserImage(Bitmap image) {
        profilePic.setImageBitmap(Tools.getResizedBitmap(image, 250));
    }

    // TODO password
    private void updateUserSettings() {
        //updateUserPassword();
        try {
            setNewInformation();
        } catch (Exception e) {

        }

    }

    private void setNewInformation() throws Exception {
        User currentUser = Store.getCurrentUser();
        currentUser.setFirstName(firstName.getText().toString());
        currentUser.setLastName(lastName.getText().toString());
        currentUser.setPhoneNumber(phoneNumber.getText().toString());
        currentUser.setDescription(description.getText().toString());
        currentUser.setSmokes(smokes.isChecked());
        currentUser.setHasPets(hasPets.isChecked());
        currentUser.updateUser();
    }

    private void updateUserPassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = password.getText().toString();

        if (currentPassword.getText().toString().length() > 0 &&
                password.getText().toString().length() > 0 &&
                repeatPassword.getText().toString().length() > 0) {

            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User password updated.");
                            }
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            ImageView imageView = getView().findViewById(R.id.imageView);
            imageView.setImageURI(selectedImage);
        }
    }
}