package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;
import android.media.MediaSession2Service;
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
    String title = "";
    String message = "";

    public RemoveAccommDialogDisplayer(Context context, View removedView) {
        super(context, -1, -1, null);
        this.removedView = removedView;
        this.title = context.getString(R.string.remove_listing_title);
        this.message = context.getString(R.string.remove_listing_message);
    }

    public RemoveAccommDialogDisplayer(Context context, View removedView, String title, String message) {
        super(context, -1, -1, null);
        this.removedView = removedView;
        this.title = title;
        this.message = message;
    }

    @Override
    protected void positiveFunctionality() {
        dialog.dismiss();
    }

    @Override
    protected void setDialogProperties() {
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.ic_warning_filled);
        dialog.setTitle(title);
        dialog.setMessage(message);
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