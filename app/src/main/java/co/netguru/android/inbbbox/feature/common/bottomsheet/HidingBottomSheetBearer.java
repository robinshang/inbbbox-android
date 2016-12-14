package co.netguru.android.inbbbox.feature.common.bottomsheet;

import android.support.v4.app.Fragment;

@FunctionalInterface
public interface HidingBottomSheetBearer {

    void showBottomSheet(Fragment fragment, String tag);
}
