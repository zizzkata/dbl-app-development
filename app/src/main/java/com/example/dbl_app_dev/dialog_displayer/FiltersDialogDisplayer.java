package com.example.dbl_app_dev.dialog_displayer;

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

    public FiltersDialogDisplayer(Context context,
                                  int cancelId, int positiveId, int negativeId, View myView, Filters filters) {
        super(context, cancelId, positiveId, negativeId, myView);
        this.filters = filters;
    }

    @Override
    protected void positiveFunctionality() {
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

        Toast.makeText(context, "Filters Saved!", Toast.LENGTH_SHORT).show();
        Log.d("extra_debug", filters.toString());
        dialog.dismiss();
    }
}
