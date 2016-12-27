package co.netguru.android.inbbbox.common.base.bottomsheet;

import android.support.v4.app.Fragment;

@FunctionalInterface
public interface HidingBottomSheetBearer {

    void showBottomSheet(Fragment fragment, String tag);
}
