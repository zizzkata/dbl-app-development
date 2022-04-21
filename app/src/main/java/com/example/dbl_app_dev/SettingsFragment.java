package com.example.dbl_app_dev;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Fragment that handles the logic of the Settings page
 */
public class SettingsFragment extends Fragment {

    // Credentials views
    private EditText email;
    private EditText username;
    private EditText password;

    // Warnings views
    private TextView currentPasswordWarning;
    private TextView passwordWarning;
    private TextView repeatPasswordWarning;

    // User details views
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private EditText description;
    private CheckBox smokes;
    private CheckBox hasPets;
    private ImageView profilePic;
    private EditText gender;

    // All used validators
    private ViewValidator currPassValidator;
    private ViewValidator passValidator;
    private ViewValidator repPassValidator;

    public SettingsFragment() {
        // Required empty public constructor
    }

    private void init() {
        // Get views by id
        EditText currentPassword = getView().findViewById(R.id.currentPasswordBox);
        EditText repeatPassword = getView().findViewById(R.id.ConfirmNewPasswordBox);

        // Get the corresponding views
        email = getView().findViewById(R.id.editTextTextEmailAddress);
        username = getView().findViewById(R.id.usernameBox);
        currentPasswordWarning = getView().findViewById(R.id.invalidCurrentPassword);
        password = getView().findViewById(R.id.newPasswordBox);
        passwordWarning = getView().findViewById(R.id.invalidPassword);
        repeatPasswordWarning = getView().findViewById(R.id.invalidRepeatPassword);
        gender = getView().findViewById(R.id.genderBox);
        firstName = getView().findViewById(R.id.firstNameBox);
        lastName = getView().findViewById(R.id.lastNameBox);
        profilePic = getView().findViewById(R.id.imageView);
        phoneNumber = getView().findViewById(R.id.changePhoneBox);
        description = getView().findViewById(R.id.descriptionBox);
        smokes = getView().findViewById(R.id.smokingCheckBox);
        hasPets = getView().findViewById(R.id.havePetsCheckBox);

        // initialize validators
        currPassValidator = new currPasswordValidator(currentPassword, currentPasswordWarning);
        passValidator = new PasswordValidator(password, passwordWarning);
        repPassValidator = new RepeatPasswordValidator(repeatPassword, password,
                repeatPasswordWarning);

        // re-run validators on text change
        password.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passValidator.validate();
            }
        });

        // add password validator
        password.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                repPassValidator.validate();
            }
        });

        // add repeat password validator
        repeatPassword.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                repPassValidator.validate();
            }
        });
    }

    /**
     * Makes all possible (user-visible) warnings invisible
     */
    private void makeWarningsInvisible() {
        currentPasswordWarning.setVisibility(View.GONE);
        passwordWarning.setVisibility(View.GONE);
        repeatPasswordWarning.setVisibility(View.GONE);
    }


    /**
     * @return whether the current changes are valid (all validators pass)
     */
    private boolean areChangesValid() {
        // list of all validator objects
        ArrayList<ViewValidator> validators = new ArrayList<>();

        // add all validators to list
        validators.add(currPassValidator);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container,
                false);
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
                // get information of the current user
                user = Store.getCurrentUser();
                // display user information
                getActivity().runOnUiThread(() -> bindUserData(user));
            } catch (Exception e) {
                Log.e("OPS", "OPS");
                e.printStackTrace();
                return;
            }

            Bitmap profilePic = user.getProfilePic();
            try {
                // display a default 'empty' profile picture if user has not uploaded one yet
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

        // Add image button functionality
        TextView addImage = getView().findViewById(R.id.addImageBtn);
        addImage.setOnClickListener(view14 -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 3);
        });

        // Sign Up button leading to RegisterActivity
        TextView logOutBtn = getView().findViewById(R.id.logoutBtn);
        logOutBtn.setOnClickListener(view1 -> {
            ((MainNavigationActivity) getActivity()).logoutDialog();
        });

        // Save button used to update current user's password
        TextView saveButtonPass = getView().findViewById(R.id.saveBtnPass);
        saveButtonPass.setOnClickListener(view12 -> {
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
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(),
                            "Password Updated!", Toast.LENGTH_SHORT).show());
                } catch (Exception ignored) {
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(),
                            "Error! Password Not Updated", Toast.LENGTH_SHORT).show());
                }
            });
        });

        // Save button used to update current user's personal info
        TextView saveButton = getView().findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(view13 -> AsyncWrapper.wrap(() -> {
            try {
                setNewInformation();
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(),
                        "Account Details Saved!", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                // ERR show
                Log.e("updateUserInformation", e.getMessage());

                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Error!",
                        Toast.LENGTH_SHORT).show());
            }
        }));
    }

    /**
     * Set the views' text based on the user's data
     *
     * @param user
     */
    private void bindUserData(User user) {
        email.setText(user.getEmail());
        username.setText(user.getUsername());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        phoneNumber.setText(user.getPhoneNumber());
        description.setText(user.getDescription());
        smokes.setChecked(user.smokes());
        hasPets.setChecked(user.hasPets());
        gender.setText(user.getGender());
    }

    /**
     * Set the profile pic of the user in the UI
     *
     * @param image
     */
    private void bindUserImage(Bitmap image) {
        profilePic.setImageBitmap(Tools.getResizedBitmap(image, 250));
    }

    /**
     * Updates the current user's information
     *
     * @modifies User
     */
    private void setNewInformation() throws Exception {
        User currentUser = Store.getCurrentUser();
        currentUser.setFirstName(firstName.getText().toString());
        currentUser.setLastName(lastName.getText().toString());
        currentUser.setPhoneNumber(phoneNumber.getText().toString());
        currentUser.setDescription(description.getText().toString());
        currentUser.setGender(gender.getText().toString());
        currentUser.setSmokes(smokes.isChecked());
        currentUser.setHasPets(hasPets.isChecked());
        currentUser.updateUser();
    }

    /**
     * Updates the current password of the user by sending the text in the 'password' textbox
     * to the database
     */
    private void updateUserPassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = password.getText().toString();

        assert user != null;
        user.updatePassword(newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "User password updated.");
            }
        });
    }

    /**
     * Update the user's profile photo from their gallery
     * @param requestCode
     * @param data
     * @param resultCode
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            ImageView imageView = getView().findViewById(R.id.imageView);
            imageView.setImageURI(selectedImage);

            AsyncWrapper.wrap(() -> {
                try {
                    Store.getCurrentUser().updateUserImage(
                            ((BitmapDrawable) imageView.getDrawable()).getBitmap());
                } catch (Exception e) {
                    Log.e("Image", e.getMessage());
                }
            });
        }
    }
}