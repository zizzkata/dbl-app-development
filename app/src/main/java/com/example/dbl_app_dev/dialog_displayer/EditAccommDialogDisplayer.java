package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dbl_app_dev.R;

/**
 * Concrete class of dialog_displayer
 * used to open popup dialog for editing an existing accommodation.
 */
public class EditAccommDialogDisplayer extends DialogDisplayer {
    // Accommodation object that is passed to the remove accommodation dialog
    View accommObject;

    public EditAccommDialogDisplayer(Context context,
                                     int cancelId, int positiveId, int negativeId,
                                     View myView, View accommObject) {
        super(context, cancelId, positiveId, negativeId, myView);
        this.accommObject = accommObject;
    }

    @Override
    protected void positiveFunctionality() {
        Toast.makeText(context, "Current Accommodation Edited", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    protected void additionalFunctionality() {
        Button removeListingButton = myView.findViewById(R.id.removeListingButton);
        removeListingButton.setOnClickListener(view1 -> {
            // If the remove listing button is pressed then open a remove accommodation dialog
            // with this dialog and the accommodation object as parameters
            (new RemoveAccommDialogDisplayer(context, accommObject, dialog))
                    .displayPopupDialog();
        });
    }
}
