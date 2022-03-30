package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dbl_app_dev.MainNavigationActivity;
import com.example.dbl_app_dev.R;

/**
 * Concrete class of dialog_displayer
 * used to open popup dialog for editing an existing accommodation.
 */
public class EditAccommDialogDisplayer extends DialogDisplayer {
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
            // If the remove listing button is pressed then close both dialogs and remove the listing
            (new RemoveAccommDialogDisplayer(context, accommObject, dialog))
                    .displayPopupDialog();
        });
    }
}
