package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;
import android.view.View;

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
    protected void positiveFunctionality() {
        // If the remove listing button is pressed then close both dialogs and remove the listing
        (new RemoveAccommDialogDisplayer(context, accommObject, dialog))
                .displayPopupDialog();
    }
}
