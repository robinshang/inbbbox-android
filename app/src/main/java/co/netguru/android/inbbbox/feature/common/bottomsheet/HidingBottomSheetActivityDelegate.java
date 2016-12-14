package co.netguru.android.inbbbox.feature.common.bottomsheet;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.View;

import co.netguru.android.inbbbox.utils.InputUtils;

public class HidingBottomSheetActivityDelegate implements HidingBottomSheetBearer {

    private static final String BOTTOM_SHEET_STATE = "bottomSheetState";
    private final BottomSheetActivityCallback bottomSheetActivityCallback;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private View bottomSheetView;


    public HidingBottomSheetActivityDelegate(BottomSheetActivityCallback bottomSheetActivityCallback,
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
        bottomSheetActivityCallback.replaceFragment(bottomSheetView.getId(), fragment, tag).commit();
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
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    InputUtils.hideKeyboard(bottomSheetActivityCallback.getApplicationContext(), bottomSheetView);
                    bottomSheetActivityCallback.removeFragmentFromView(bottomSheetView.getId());
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //no-op
            }
        };
    }
}
