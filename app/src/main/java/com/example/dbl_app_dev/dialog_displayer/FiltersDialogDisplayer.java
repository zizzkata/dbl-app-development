package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Concrete class of dialog_displayer
 * used to open a filter pop-up for the Tenant discovery page
 */
public class FiltersDialogDisplayer extends DialogDisplayer {
    public FiltersDialogDisplayer(Context context,
                                       int cancelId, int positiveId, int negativeId, View myView) {
        super(context, cancelId, positiveId, negativeId, myView);
    }

    @Override
    protected void positiveFunctionality() {
        Toast.makeText(context, "Filters Saved!", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
