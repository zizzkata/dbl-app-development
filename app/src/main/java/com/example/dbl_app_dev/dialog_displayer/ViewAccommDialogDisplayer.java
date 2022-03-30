package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.example.dbl_app_dev.R;

/**
 * Concrete class of dialog_displayer
 * used to open popup dialog to view an existing accommodation.
 */
public class ViewAccommDialogDisplayer extends DialogDisplayer {
    View accommObject;

    public ViewAccommDialogDisplayer(Context context, int cancelId, int positiveId, View myView, View accommObject) {
        super(context, cancelId, positiveId, myView);

        this.accommObject = accommObject;
    }

    @Override
    protected void setCancelable() {
        dialog.setCancelable(true);
    }

    @Override
    protected void positiveFunctionality() {
        // If the remove listing button is pressed then close both dialogs and remove the listing
        (new RemoveAccommDialogDisplayer(context, accommObject, dialog))
                .displayPopupDialog();
    }
}
