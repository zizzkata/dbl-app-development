package com.example.dbl_app_dev.dialog_displayer;

import android.content.Context;
import android.view.View;

import com.example.dbl_app_dev.R;

/**
 * Concrete class of dialog_displayer
 * used to open popup dialog for the settings in the liked listings
 */
public class LikedTenantSettingsDialogDisplayer extends DialogDisplayer {
    View parentView;

    public LikedTenantSettingsDialogDisplayer(Context context,
                                              int cancelId, int positiveId, int negativeId,
                                              View myView, View parentView) {
        super(context, cancelId, positiveId, negativeId, myView);
        this.parentView = parentView;
    }

    @Override
    protected void positiveFunctionality() {
        if (((androidx.appcompat.widget.SwitchCompat)
                myView.findViewById(R.id.showPositiveListingsSwitch)).isChecked()) {
            parentView.findViewById(R.id.positiveListingsContainer).setVisibility(View.VISIBLE);
        } else {
            parentView.findViewById(R.id.positiveListingsContainer).setVisibility(View.GONE);
        }

        if (((androidx.appcompat.widget.SwitchCompat)
                myView.findViewById(R.id.showNeutralListingsSwitch)).isChecked()) {
            parentView.findViewById(R.id.neutralListingsContainer).setVisibility(View.VISIBLE);
        } else {
            parentView.findViewById(R.id.neutralListingsContainer).setVisibility(View.GONE);
        }

        if (((androidx.appcompat.widget.SwitchCompat)
                myView.findViewById(R.id.showNegativeListingsSwitch12)).isChecked()) {
            parentView.findViewById(R.id.negativeListingsContainer).setVisibility(View.VISIBLE);
        } else {
            parentView.findViewById(R.id.negativeListingsContainer).setVisibility(View.GONE);
        }

        dialog.dismiss();
    }
}