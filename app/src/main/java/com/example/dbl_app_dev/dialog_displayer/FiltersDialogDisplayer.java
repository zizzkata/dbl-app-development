package com.example.dbl_app_dev.dialog_displayer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dbl_app_dev.R;
import com.example.dbl_app_dev.store.Store;
import com.example.dbl_app_dev.util.Filters;

/**
 * Concrete class of dialog_displayer
 * used to open a filter pop-up for the Tenant discovery page
 */
public class FiltersDialogDisplayer extends DialogDisplayer {
    // Filters object to remember all current filters
    final Filters filters;

    // Runnable reset object
    final Runnable reset;

    @SuppressLint("SetTextI18n")
    public FiltersDialogDisplayer(Context context,
                                  int cancelId, int positiveId, int negativeId,
                                  View myView, Filters filters, Runnable reset) {
        super(context, cancelId, positiveId, negativeId, myView);
        this.filters = filters;
        this.reset = reset;

        // Set default values for checkboxes and text fields.
        ((CheckBox) myView.findViewById(R.id.furnishedCheckBox)).setChecked(filters.isFurnished());
        ((EditText) myView.findViewById(R.id.minPriceTxt))
                .setText(filters.getPriceLower().toString());
        ((EditText) myView.findViewById(R.id.maxPriceTxt))
                .setText(filters.getPriceUpper().toString());
        ((EditText) myView.findViewById(R.id.minMetersTxt))
                .setText(filters.getAreaLower().toString());
        ((EditText) myView.findViewById(R.id.maxMetersTxt))
                .setText(filters.getAreaUpper().toString());
        ((EditText) myView.findViewById(R.id.cityTxt)).setText(filters.getCity());
    }

    @Override
    protected void positiveFunctionality() {
        // Set the city to be filtered
        this.filters.setCity(((EditText) myView.findViewById(R.id.cityTxt)).getText().toString());

        // Set furnished status to be filtered
        this.filters.setFurnished(((CheckBox) myView
                .findViewById(R.id.furnishedCheckBox)).isChecked());

        // Set minimum price to be filtered
        // if not specified set it to 0
        try {
            this.filters.setPriceLower(Long.parseLong(((EditText) myView
                    .findViewById(R.id.minPriceTxt)).getText().toString()));
        } catch (NumberFormatException e) {
            this.filters.setPriceLower(0L);
        }

        // Set maximum price to be filtered
        // if not specified set it to 0
        try {
            this.filters.setPriceUpper(Long.parseLong(((EditText) myView
                    .findViewById(R.id.maxPriceTxt)).getText().toString()));
        } catch (NumberFormatException e) {
            this.filters.setPriceUpper(0L);
        }

        // Set minimum area to be filtered
        // if not specified set it to 0
        try {
            this.filters.setAreaLower(Long.parseLong(((EditText) myView
                    .findViewById(R.id.minMetersTxt)).getText().toString()));
        } catch (NumberFormatException e) {
            this.filters.setPriceLower(0L);
        }

        // Set maximum area to be filtered
        // if not specified set it to 0
        try {
            this.filters.setAreaUpper(Long.parseLong(((EditText) myView
                    .findViewById(R.id.maxMetersTxt)).getText().toString()));
        } catch (NumberFormatException e) {
            this.filters.setPriceUpper(0L);
        }

        // If the minimum and maximum price have not been set then do not save the filters
        // otherwise save the filters and dismiss the dialog
        if (((EditText) myView.findViewById(R.id.minPriceTxt)).getText().toString().equals("")
                || ((EditText) myView.findViewById(R.id.maxPriceTxt))
                .getText().toString().equals("")) {
            Toast.makeText(context, "Filters not saved.", Toast.LENGTH_LONG).show();
            Store.resetFilters();
            reset.run();
        } else {
            Store.setFilters(this.filters.getPriceLower(), this.filters.getPriceUpper());
            Toast.makeText(context, "Filters Saved!", Toast.LENGTH_SHORT).show();
            reset.run();
            dialog.dismiss();
        }
    }

    @Override
    protected void createDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        if (myView != null) {
            dialogBuilder.setView(myView);
        }
        dialog = dialogBuilder.create();
    }
}
