package co.netguru.android.inbbbox.feature.common.bottomsheet;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.View;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.utils.InputUtils;

public class BottomSheetActivityDelegate implements BottomSheetBearer {

    private static final String BOTTOM_SHEET_STATE = "bottomSheetState";
    private final BottomSheetActivityCallback bottomSheetActivityCallback;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private View bottomSheetView;


    public BottomSheetActivityDelegate(BottomSheetActivityCallback bottomSheetActivityCallback,
                                       @NonNull View bottomSheetView) {
        this.bottomSheetActivityCallback = bottomSheetActivityCallback;
        this.bottomSheetView = bottomSheetView;
        initializeBottomSheetBehavior();
    }

    private void initializeBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setSkipCollapsed(true);
        bottomSheetBehavior.setBottomSheetCallback(createBottomSheetCallback());
    }

    @Override
    public void showBottomSheet(Fragment fragment, String tag) {
        bottomSheetActivityCallback.replaceFragment(R.id.fragment_container, fragment, tag).commit();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(BOTTOM_SHEET_STATE, bottomSheetBehavior.getState());
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        bottomSheetBehavior.setState(savedInstanceState.getInt(BOTTOM_SHEET_STATE,
                BottomSheetBehavior.STATE_COLLAPSED));
    }

    public boolean isBottomSheetOpen() {
        return bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }

    public void hideBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private BottomSheetBehavior.BottomSheetCallback createBottomSheetCallback() {
        return new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    InputUtils.hideKeyboard(bottomSheetActivityCallback.getApplicationContext(), bottomSheetView);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //no-op
            }
        };
    }
}
