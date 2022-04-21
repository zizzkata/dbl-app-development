package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;

import com.example.dbl_app_dev.MainNavigationActivity;
import com.example.dbl_app_dev.R;

/**
 * Concrete class of dialog_displayer
 * used to open popup dialog for logging out
 */
public class EditSettingsDialogDisplayer extends DialogDisplayer {
    String title;
    String message;
    MainNavigationActivity mainNavigationActivity;

    public EditSettingsDialogDisplayer(Context context) {
        super(context, -1, -1, -1, null);
        this.title = context.getString(R.string.edit_settings_title);
        this.message = context.getString(R.string.edit_settings_message);
        mainNavigationActivity = (MainNavigationActivity) context;
    }

    @Override
    protected void setDialogProperties() {
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_warning_filled);
        dialog.setTitle(title);
        dialog.setMessage(message);

        dialog.setButton(dialog.BUTTON_POSITIVE, "Edit Settings",
                (dialog, which) -> {
                    mainNavigationActivity.openSettingsFragment();
                    dialog.dismiss();
                });
    }
}
