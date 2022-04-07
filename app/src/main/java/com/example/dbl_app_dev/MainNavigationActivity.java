package com.example.dbl_app_dev;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.AlertDialog;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.dbl_app_dev.dialog_displayer.CreateAccommDialogDisplayer;
import com.example.dbl_app_dev.dialog_displayer.EditAccommDialogDisplayer;
import com.example.dbl_app_dev.dialog_displayer.LikedTenantSettingsDialogDisplayer;
import com.example.dbl_app_dev.dialog_displayer.RemoveAccommDialogDisplayer;
import com.example.dbl_app_dev.dialog_displayer.ViewAccommDialogDisplayer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        linkNavControllerToNavBar();
    }

    /**
     * Method used for linking the navigation controller to the navigation bar
     */
    private void linkNavControllerToNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    /**
     * Encompasses the logic for changing the current mode (Tenant/Landlord) and
     * showing the correct icons on the nav bar.
     */
    public void modeSwitchLogic(SwitchCompat modeSwitch) {
        // Switch view
        // SwitchCompat modeSwitch = findViewById(R.id.modeSwitch);

        // Getting the menu view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Menu navBarMenu = bottomNavigationView.getMenu();

        // Getting the needed menu items
        MenuItem tenantLikedItem = navBarMenu.findItem(R.id.tenantLikedFragment);
        MenuItem landlordListingsItem = navBarMenu.findItem(R.id.landlordAccommodationManagementFragment);
        MenuItem tenantDiscoverItem = navBarMenu.findItem(R.id.tenantDiscoverFragment);
        MenuItem landlordDiscoverItem = navBarMenu.findItem(R.id.landlordDiscoverFragment);

        modeSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            // isChecked is true when in Landlord mode
            // Set correct items for nav bar based on the mode
            tenantLikedItem.setVisible(!isChecked);
            tenantDiscoverItem.setVisible(!isChecked);
            landlordListingsItem.setVisible(isChecked);
            landlordDiscoverItem.setVisible(isChecked);

            // Set text on switch based on the mode
            String switchText = isChecked ? "Landlord" : "Tenant";
            modeSwitch.setText(switchText);
        });
    }

    /**
     * Method used to create the popup that opens the tenant liked settings
     */
    public AlertDialog openLikedTenantSettingsDialog() {
        View myView = getLayoutInflater().inflate(R.layout.liked_tenant_settings_pop_up, null);
        return (new LikedTenantSettingsDialogDisplayer(this,
                R.id.cancelButton, R.id.saveButton, R.id.negativeButton,
                myView, findViewById(R.id.scrollConstraintLayout)))
                .displayPopupDialog();
    }

    /**
     * Method used to create the popup that shows when adding a new accommodation.
     */
    public void createNewAccommodationDialog() {
        View myView = getLayoutInflater().inflate(R.layout.new_accommodation_pop_up, null);
        (new CreateAccommDialogDisplayer(this,
                R.id.cancelButton, R.id.createButton, R.id.negativeButton, myView))
                .displayPopupDialog();
    }

    /**
     * Method used to create the popup that shows when editing an existing
     * accommodation.
     */
    public void editAccommodationDialog(View accommObject) {
        View myView = getLayoutInflater().inflate(R.layout.edit_accommodation_pop_up, null);
        // (new EditAccommDialogDisplayer(this, R.id.cancelButton, R.id.saveBtn,
        // myView))
        (new EditAccommDialogDisplayer(this,
                R.id.cancelButton, R.id.saveBtn, R.id.negativeButton, myView, accommObject))
                .displayPopupDialog();
    }

    /**
     * Method used to create the popup that shows an existing accommodation.
     */

    public AlertDialog viewAccommodationDialog(View accommObject) {
        View myView = getLayoutInflater().inflate(R.layout.view_accommodation_pop_up, null);
        return (new ViewAccommDialogDisplayer(this,
                R.id.cancelButton, R.id.saveButton, myView, accommObject))
                .displayPopupDialog();
    }

    /**
     * Method used to create the popup that shows when removing a liked
     * accommodation.
     */
    public void removeAccommodationDialog(View removedView) {
        (new RemoveAccommDialogDisplayer(this, removedView))
                .displayPopupDialog();
    }

    /**
     * Method used to create the popup that shows when adding a new accommodation.
     */
    public void openFilterDialog() {
        View myView = getLayoutInflater().inflate(R.layout.activity_filters, null);
        (new CreateAccommDialogDisplayer(this, R.id.cancelButton, R.id.saveBtn, -1, myView))
                .displayPopupDialog();
    }
}