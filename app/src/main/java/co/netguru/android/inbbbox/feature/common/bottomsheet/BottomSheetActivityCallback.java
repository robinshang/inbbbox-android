package co.netguru.android.inbbbox.feature.common.bottomsheet;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public interface BottomSheetActivityCallback {

    Context getApplicationContext();

    FragmentTransaction replaceFragment(@IdRes int fragmentContainer, Fragment fragment, String tag);
}
