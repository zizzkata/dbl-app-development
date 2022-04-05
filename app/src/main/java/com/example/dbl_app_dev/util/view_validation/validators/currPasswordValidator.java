package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

public final class currPasswordValidator extends PasswordValidator{
    public currPasswordValidator(View toValidate, TextView warning) {
        super(toValidate, warning);

    }
}
