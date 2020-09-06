package com.tim32.emarket.util;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.tim32.emarket.MainActivity_;

public class FragmentUtil {

    public static final String TAG = FragmentUtil.class.getName();

    public static void setRootFragment(Fragment currentFragment, Fragment newFragment) {
        Fragment currentRootFragment = currentFragment;
        while (currentRootFragment.getParentFragment() != null) {
            currentRootFragment = currentRootFragment.getParentFragment();
        }
        FragmentActivity activity = currentRootFragment.getActivity();
        if (!(activity instanceof MainActivity_)) {
            Log.e(TAG, "Root fragment should be in MainActivity.");
        }
        MainActivity_ mainActivity_ = (MainActivity_) activity;
        mainActivity_.setCurrentRootFragment(newFragment);
    }

    public static void setRootFragment(Fragment currentFragment, Fragment newFragment, Bundle bundle) {
        newFragment.setArguments(bundle);
        setRootFragment(currentFragment, newFragment);
    }

    public static void performBackAction(FragmentActivity activity) {
        if (activity != null) {
            activity.getSupportFragmentManager().popBackStack();
        }
    }
}
