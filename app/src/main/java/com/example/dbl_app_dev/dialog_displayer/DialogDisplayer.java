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
    private final int cancelId; // set to -1 if it is not needed
    private final int positiveId; // set to -1 if it is not needed
    private final int negativeId; // set to -1 if it is not needed
    public final View myView; // set to null if it is not needed

    DialogDisplayer(Context context, int cancelId, int positiveId, int negativeId, View myView) {
        this.context = context;
        this.cancelId = cancelId;
        this.positiveId = positiveId;
        this.negativeId = negativeId;
        this.myView = myView;
    }

    // Template method
    public AlertDialog displayPopupDialog() {
        // Set-up dialog
        createDialog();
        setCancelable();
        setDialogProperties();
        dialog.show();

        // "Cancel" button functionality
        if (cancelId != -1) {
            Button cancelButton = myView.findViewById(cancelId);
            cancelButton.setOnClickListener(view -> cancelFunctionality());
        }

        // "Positive" button functionality
        if (positiveId != -1) {
            Button positiveButton = myView.findViewById(positiveId);
            positiveButton.setOnClickListener(view -> positiveFunctionality());
        }

        // "Negative" button functionality
        if (negativeId != -1) {
            Button positiveButton = myView.findViewById(negativeId);
            positiveButton.setOnClickListener(view -> negativeFunctionality());
        }

        additionalFunctionality();

        return dialog;
    }

    protected void additionalFunctionality() {
    }

    ;

    protected void createDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        if (myView != null) {
            dialogBuilder.setView(myView);
        }
        dialog = dialogBuilder.create();
    }

    protected void setCancelable() {
        dialog.setCancelable(false);
    }

    protected void setDialogProperties() {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupDialogAnimation;
    }

    protected void cancelFunctionality() {
        dialog.dismiss();
    }

    protected void positiveFunctionality() {
        dialog.dismiss();
    }

    protected void negativeFunctionality() {
        dialog.dismiss();
    }
}
