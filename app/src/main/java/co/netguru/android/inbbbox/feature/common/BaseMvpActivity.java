package co.netguru.android.inbbbox.feature.common;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.event.CriticalLogoutEvent;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import co.netguru.android.inbbbox.utils.InputUtils;
import rx.Subscription;

public abstract class BaseMvpActivity<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpActivity<V, P> {

    private static final String BOTTOM_SHEET_STATE = "bottomSheetState";

    @BindView(android.R.id.content)
    View contentView;
    @Nullable
    @BindView(R.id.fragment_container)
    View bottomSheetView;

    @Nullable
    private BottomSheetBehavior bottomSheetBehavior;
    private Subscription criticalLogoutSubscription;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initializeBottomSheet();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (bottomSheetBehavior != null) {
            outState.putInt(BOTTOM_SHEET_STATE, bottomSheetBehavior.getState());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(savedInstanceState.getInt(BOTTOM_SHEET_STATE,
                    BottomSheetBehavior.STATE_COLLAPSED));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        criticalLogoutSubscription = App.getAppComponent(this).rxBus()
                .getEvents(CriticalLogoutEvent.class)
                .compose(RxTransformers.androidIO())
                .subscribe(this::handleUnauthorisedEvent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        criticalLogoutSubscription.unsubscribe();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior != null && isBottomSheetOpen()) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        super.onBackPressed();
    }

    protected FragmentTransaction replaceFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, fragment, tag);
        return ft;
    }

    protected void showTextOnSnackbar(@StringRes int stringRes) {
        Snackbar.make(contentView, stringRes, Snackbar.LENGTH_LONG).show();
    }

    protected void showBottomSheet(Fragment fragment, String tag) {
        if (bottomSheetBehavior != null) {
            replaceFragment(R.id.fragment_container, fragment, tag).commit();
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private void handleUnauthorisedEvent(CriticalLogoutEvent object) {
        Toast.makeText(this, object.getReason(), Toast.LENGTH_SHORT).show();
        LoginActivity.startActivityClearTask(this);
        finish();
    }

    private boolean isBottomSheetOpen() {
        return bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }

    private void initializeBottomSheet() {
        if (bottomSheetView != null) {
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        InputUtils.hideKeyboard(BaseMvpActivity.this, bottomSheetView);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    //no-op
                }
            });
        }
    }
}