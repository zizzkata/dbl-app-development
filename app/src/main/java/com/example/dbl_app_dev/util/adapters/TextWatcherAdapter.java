package com.example.dbl_app_dev.util.adapters;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Adapter class for TextWatcher
 */
public abstract class TextWatcherAdapter implements TextWatcher {
    /**
     * @param charSequence a given sequence of characters
     * @param i the first index
     * @param i1 the (aptly-named) second index
     * @param i2 the third index
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    /**
     * @param charSequence a given sequence of characters
     * @param i the first index
     * @param i1 the (aptly-named) second index
     * @param i2 the third index
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    /**
     * @param editable an editable field
     */
    @Override
    public void afterTextChanged(Editable editable) {
    }
}