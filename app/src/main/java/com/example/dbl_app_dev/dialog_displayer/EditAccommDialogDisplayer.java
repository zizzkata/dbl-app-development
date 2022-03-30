package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Concrete class of dialog_displayer
 * used to open popup dialog for editing an existing accommodation.
 */
public class EditAccommDialogDisplayer extends DialogDisplayer {
    public EditAccommDialogDisplayer(Context context,
                                     int cancelId, int positiveId, int negativeId, View myView) {
        super(context, cancelId, positiveId, negativeId, myView);
    }

    @Override
    protected void positiveFunctionality() {
        Toast.makeText(context, "Current Accommodation Edited", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
