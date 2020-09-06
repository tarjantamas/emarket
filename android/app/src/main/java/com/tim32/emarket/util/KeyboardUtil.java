package com.tim32.emarket.util;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class KeyboardUtil {

    private static final String TAG = KeyboardUtil.class.getName();

    public static void hideKeyboard(Activity activity) {
        if (activity == null) {
            Log.i(TAG, "Unable to hide keyboard, provided activity is null.");
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private KeyboardUtil() {
    }
}
