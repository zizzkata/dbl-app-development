package com.example.dbl_app_dev;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import com.example.dbl_app_dev.util.view_validation.validators.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.example.dbl_app_dev.util.adapters.TextWatcherAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.dbl_app_dev.store.Store;
import com.example.dbl_app_dev.store.objects.User;
import com.example.dbl_app_dev.util.AsyncWrapper;
import com.example.dbl_app_dev.util.Tools;
import com.example.dbl_app_dev.util.view_validation.validators.PasswordValidator;
import com.example.dbl_app_dev.util.view_validation.validators.RepeatPasswordValidator;
import com.example.dbl_app_dev.util.view_validation.validators.ViewValidator;
import com.example.dbl_app_dev.util.view_validation.validators.currPasswordValidator;

import java.util.ArrayList;

import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    //User currentUser = new User()
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        //validators.add(emailValidator);
        //validators.add(new UsernameUniquenessValidator(username, usernameWarning));
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment thirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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
        //makeWarningsInvisible();
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
                    InputStream stream =  getContext()
                            .getAssets().open("default-user-image.png");
                    profilePic = BitmapFactory.decodeStream(stream);
                }
                Bitmap finalProfilePic = profilePic; // necessary for javalint to run ;/
                getActivity().runOnUiThread(() -> bindUserImage(finalProfilePic));
            } catch (Exception e) { // never happens
                Log.e("FATAL", e.getMessage());
                e.printStackTrace();
            }
        });

        // Sign Up button leading to RegisterPage
        TextView signUpTxt = getView().findViewById(R.id.logoutBtn);
        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginPage.class));
            }
        });

        // Save button used to update current user's personal info and password
        TextView saveButton = getView().findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!areChangesValid()) {
                    Context context = getActivity().getApplicationContext();
                    String text = "shit";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    return;
                }

                try {
                    updateUserSettings();
                } catch (Exception e) {

                }

                Context context = getActivity().getApplicationContext();
                String text = "ok";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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

    private void updateUserSettings() {
        updateUserPassword();
    }

    private void updateUserPassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = password.getText().toString();

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