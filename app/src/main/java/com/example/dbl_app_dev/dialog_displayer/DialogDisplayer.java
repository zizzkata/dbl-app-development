package com.example.dbl_app_dev.dialog_displayer;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;

import com.example.dbl_app_dev.R;

/**
 * Abstract class using the Template Method design pattern
 * to have a configurable way to create a dialog pop-up.
 */
abstract class DialogDisplayer {
    public AlertDialog dialog;
    public Context context;
    private final int cancelId;
    private final int positiveId;
    private final View myView;

    DialogDisplayer(Context context, int cancelId, int positiveId, View v) {
        this.context = context;
        this.cancelId = cancelId;
        this.positiveId = positiveId;
        this.myView = v;
    }

    // Template method
    public void displayPopupDialog() {
        // Set-up dialog
        createDialog(myView);
        setDialogProperties();
        dialog.show();

        // "Cancel" button functionality
        Button cancelButton = myView.findViewById(cancelId);
        cancelButton.setOnClickListener(view -> cancelFunctionality());

        // "Postive" button functionality
        Button createButton = myView.findViewById(positiveId);
        createButton.setOnClickListener(view -> positiveFunctionality());
    }

    protected abstract void positiveFunctionality();

    protected void createDialog(View _myView) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(_myView);
        dialog = dialogBuilder.create();
    }

    protected void setDialogProperties() {
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupDialogAnimation;
    }

    protected void cancelFunctionality() {
        dialog.dismiss();
    }
}
