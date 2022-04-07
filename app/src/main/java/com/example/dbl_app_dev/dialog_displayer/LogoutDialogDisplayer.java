package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;
import android.widget.Toast;

import com.example.dbl_app_dev.MainNavigationActivity;
import com.example.dbl_app_dev.R;

/**
 * Concrete class of dialog_displayer
 * used to open popup dialog for logging out
 */
public class LogoutDialogDisplayer extends DialogDisplayer {
    String title;
    String message;
    MainNavigationActivity mainNavigationActivity;

    public LogoutDialogDisplayer(Context context) {
        super(context, -1, -1, -1, null);
        this.title = context.getString(R.string.logout_title);
        this.message = context.getString(R.string.logout_message);
        mainNavigationActivity = (MainNavigationActivity) context;
    }

    @Override
    protected void setDialogProperties() {
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_warning_filled);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(dialog.BUTTON_NEUTRAL, "Cancel",
                (dialog, which) -> dialog.dismiss());
        dialog.setButton(dialog.BUTTON_POSITIVE, "Log out",
                (dialog, which) -> {
                    Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show();
                    mainNavigationActivity.logout();
                    dialog.dismiss();
                });
    }
}