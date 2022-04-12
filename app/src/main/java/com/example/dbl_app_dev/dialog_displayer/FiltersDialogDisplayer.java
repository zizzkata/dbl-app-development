package com.example.dbl_app_dev.dialog_displayer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dbl_app_dev.R;
import com.example.dbl_app_dev.util.Filters;

/**
 * Concrete class of dialog_displayer
 * used to open a filter pop-up for the Tenant discovery page
 */
public class FiltersDialogDisplayer extends DialogDisplayer {
    final Filters filters;

    @SuppressLint("SetTextI18n")
    public FiltersDialogDisplayer(Context context,
                                  int cancelId, int positiveId, int negativeId, View myView, Filters filters) {
        super(context, cancelId, positiveId, negativeId, myView);
        this.filters = filters;
        assert myView != null;
        ((CheckBox) myView.findViewById(R.id.furnishedCheckBox)).setChecked(filters.isFurnished());
        ((EditText) myView.findViewById(R.id.minPriceTxt)).setText(filters.getPriceLower().toString());
        ((EditText) myView.findViewById(R.id.maxPriceTxt)).setText(filters.getPriceUpper().toString());
        ((EditText) myView.findViewById(R.id.minMetersTxt)).setText(filters.getAreaLower().toString());
        ((EditText) myView.findViewById(R.id.maxMetersTxt)).setText(filters.getAreaUpper().toString());
        ((EditText) myView.findViewById(R.id.cityTxt)).setText(filters.getCity());
    }

    @Override
    protected void positiveFunctionality() {
        this.filters.setCity(((EditText) myView.findViewById(R.id.cityTxt)).getText().toString());
        this.filters.setFurnished(((CheckBox) myView.findViewById(R.id.furnishedCheckBox)).isChecked());
        try {
            this.filters.setPriceLower(Long.parseLong(((EditText) myView.findViewById(R.id.minPriceTxt)).getText().toString()));
        } catch (NumberFormatException e) {
            this.filters.setPriceLower(0L);
        }
        try {
            this.filters.setPriceUpper(Long.parseLong(((EditText) myView.findViewById(R.id.maxPriceTxt)).getText().toString()));
        } catch (NumberFormatException e) {
            this.filters.setPriceUpper(0L);
        }
        try {
            this.filters.setAreaLower(Long.parseLong(((EditText) myView.findViewById(R.id.minMetersTxt)).getText().toString()));
        } catch (NumberFormatException e) {
            this.filters.setPriceLower(0L);
        }
        try {
            this.filters.setAreaUpper(Long.parseLong(((EditText) myView.findViewById(R.id.maxMetersTxt)).getText().toString()));
        } catch (NumberFormatException e) {
            this.filters.setPriceUpper(0L);
        }

        Toast.makeText(context, "Filters Saved!", Toast.LENGTH_SHORT).show();
        Log.d("extra_debug", filters.toString());
        dialog.dismiss();
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
