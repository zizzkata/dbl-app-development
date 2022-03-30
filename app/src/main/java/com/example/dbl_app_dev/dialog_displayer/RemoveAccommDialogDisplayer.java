package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dbl_app_dev.R;

/**
 * Concrete class of dialog_displayer
 * used to open popup dialog for removing a liked accommodation.
 */
public class RemoveAccommDialogDisplayer extends DialogDisplayer {
    View removedView;

    public RemoveAccommDialogDisplayer(Context context, View removedView) {
        super(context, -1, -1, null);
        this.removedView = removedView;
    }

    @Override
    protected void positiveFunctionality() {
        dialog.dismiss();
    }

    @Override
    protected void setDialogProperties() {
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.ic_warning_filled);
        dialog.setTitle("Remove listing");
        dialog.setMessage("Are you sure you want to permanently remove listing from liked page!");
        dialog.setButton(dialog.BUTTON_NEGATIVE, "Cancel",
                (dialog, which) -> dialog.dismiss());
        dialog.setButton(dialog.BUTTON_POSITIVE, "Remove",
                (dialog, which) -> {
                    Toast.makeText(context, "Listing Removed", Toast.LENGTH_SHORT).show();
                    removedView.setVisibility(View.GONE);
                    dialog.dismiss();
                });
    }
}