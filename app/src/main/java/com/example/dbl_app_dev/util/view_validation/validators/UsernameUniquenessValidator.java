package com.example.dbl_app_dev.util.view_validation.validators;

import android.view.View;
import android.widget.TextView;

import com.example.dbl_app_dev.util.view_validation.scenarios.UsernameUniquenessScenario;

public final class UsernameUniquenessValidator extends UsernameValidator {
    public UsernameUniquenessValidator(View toValidate, TextView warning){
        super(toValidate, warning);
        scenarios.add(new UsernameUniquenessScenario("* There is user with" +
                " this username already"));
    }
}
