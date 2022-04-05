package com.example.dbl_app_dev;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dbl_app_dev.util.view_validation.validators.*;

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
    }

    private void makeWarningsInvisible() {
        emailWarning.setVisibility(View.GONE);
        usernameWarning.setVisibility(View.GONE);
        currentPasswordWarning.setVisibility(View.GONE);
        passwordWarning.setVisibility(View.GONE);
        repeatPasswordWarning.setVisibility(View.GONE);
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

        // Sign Up button leading to RegisterPage
        TextView signUpTxt = getView().findViewById(R.id.logoutBtn);
        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginPage.class));
            }
        });
    }
}