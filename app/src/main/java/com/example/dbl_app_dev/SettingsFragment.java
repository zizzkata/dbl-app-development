package com.example.dbl_app_dev;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dbl_app_dev.util.view_validation.validators.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.example.dbl_app_dev.util.adapters.TextWatcherAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

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
//        email = getView().findViewById(R.id.editEmailBox);
//        emailWarning = getView().findViewById(R.id.invalidEmail);
//        username = getView().findViewById(R.id.editUsernameBox);
//        usernameWarning = getView().findViewById(R.id.invalidUsername);
        currentPassword = getView().findViewById(R.id.currentPasswordBox);
        currentPasswordWarning = getView().findViewById(R.id.invalidCurrentPassword);
        password = getView().findViewById(R.id.newPasswordBox);
        passwordWarning = getView().findViewById(R.id.invalidPassword);
        repeatPassword = getView().findViewById(R.id.ConfirmNewPasswordBox);
        repeatPasswordWarning = getView().findViewById(R.id.invalidRepeatPassword);

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
        makeWarningsInvisible();

        TextView addImage = getView().findViewById(R.id.addImageBtn);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        // Logout button leading to LoginPage
        TextView logoutButton = getView().findViewById(R.id.logoutBtn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
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
                    String text = "Not saved";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    return;
                }

                boolean saved = false;
                try {
                    saved = updateUserSettings();
                } catch (Exception e) {

                }

                makeWarningsInvisible();
                Context context = getActivity().getApplicationContext();
                String text = saved ? "Saved" : "Not saved";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    private boolean updateUserSettings() {
        boolean saved = false;

        if (currentPassword.getText().toString().length() > 0 &&
                password.getText().toString().length() > 0 &&
                repeatPassword.getText().toString().length() > 0) {
            updateUserPassword();
            saved = true;
        }

        return saved;
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