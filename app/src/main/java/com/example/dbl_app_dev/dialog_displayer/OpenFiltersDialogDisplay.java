package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Concrete class of dialog_displayer
 * used to open popup dialog for adding a new accommodation.
 */
public class OpenFiltersDialogDisplay extends DialogDisplayer {
    public OpenFiltersDialogDisplay(Context context, int cancelId, int positiveId, View myView) {
        super(context, cancelId, positiveId, -1, myView);
    }

    @Override
    protected void positiveFunctionality() {
        Toast.makeText(context, "Filters have been updated", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}