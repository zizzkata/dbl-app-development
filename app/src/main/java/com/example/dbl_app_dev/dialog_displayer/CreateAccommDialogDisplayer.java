package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Concrete class of dialog_displayer
 * used to open popup dialog for adding a new accommodation.
 */
public class CreateAccommDialogDisplayer extends DialogDisplayer {
    public CreateAccommDialogDisplayer(Context context,
                                       int cancelId, int positiveId, int negativeId, View myView) {
        super(context, cancelId, positiveId, negativeId, myView);
    }

    @Override
    protected void positiveFunctionality() {
        Toast.makeText(context, "New Accommodation Created", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}