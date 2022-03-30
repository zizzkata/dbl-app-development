package com.example.dbl_app_dev.dialog_displayer;

        import android.content.Context;
        import android.view.View;
        import android.widget.Toast;

/**
 * Concrete class of dialog_displayer
 * used to open popup dialog to view an existing accommodation.
 */
public class ViewAccommDialogDisplayer extends DialogDisplayer {
    public ViewAccommDialogDisplayer(Context context, int cancelId, int positiveId, View myView) {
        super(context, cancelId, positiveId, myView);
    }

    @Override
    protected void positiveFunctionality() {
        Toast.makeText(context, "Current Accommodation Viewed", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
