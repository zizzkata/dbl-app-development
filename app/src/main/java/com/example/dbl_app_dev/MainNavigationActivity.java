package com.example.dbl_app_dev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
     * showing the correct icons on the nav bar
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
            String switchText = isChecked ? "Landlord": "Tenant";
            modeSwitch.setText(switchText);
        });
    }
}