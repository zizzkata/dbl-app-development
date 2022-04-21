package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;
import android.view.View;

/**
 * Concrete class of dialog_displayer
 * used to open popup dialog to view an existing accommodation.
 */
public class ViewAccommDialogDisplayer extends DialogDisplayer {
    // Accommodation object that is passed to the remove accommodation dialog
    View accommObject;

    public ViewAccommDialogDisplayer(Context context, int cancelId, int positiveId,
                                     View myView, View accommObject) {
        super(context, cancelId, positiveId, -1, myView);

        this.accommObject = accommObject;
    }

    @Override
    protected void setCancelable() {
        dialog.setCancelable(true);
    }

    @Override
    protected void positiveFunctionality() {
        // If the remove listing button is pressed then open a remove accommodation dialog
        // with this dialog and the accommodation object as parameters
        (new RemoveAccommDialogDisplayer(context, accommObject, dialog))
                .displayPopupDialog();
    }
}
