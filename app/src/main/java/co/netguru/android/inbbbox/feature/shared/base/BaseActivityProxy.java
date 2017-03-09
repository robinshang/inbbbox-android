package co.netguru.android.inbbbox.feature.shared.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.commons.rx.RxTransformers;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.event.events.CriticalLogoutEvent;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import co.netguru.android.inbbbox.feature.shared.base.bottomsheet.BottomSheetActivityCallback;
import co.netguru.android.inbbbox.feature.shared.base.bottomsheet.HidingBottomSheetActivityDelegate;
import rx.Subscription;

class BaseActivityProxy {

    @BindView(android.R.id.content)
    View contentView;

    @Nullable
    @BindView(R.id.bottom_sheet_fragment_container)
    View bottomSheetView;

    @Nullable
    private HidingBottomSheetActivityDelegate bottomSheetActivityDelegate;
    private AppCompatActivity activity;

    private Subscription criticalLogoutSubscription;

    BaseActivityProxy(AppCompatActivity activity) {
        this.activity = activity;
    }

    void setContentView() {
        ButterKnife.bind(activity);
        ButterKnife.bind(this, activity);

        if (bottomSheetView != null) {
            bottomSheetActivityDelegate = new HidingBottomSheetActivityDelegate(
                    (BottomSheetActivityCallback) activity, bottomSheetView);
        }
    }

    void onSaveInstanceState(Bundle outState) {
        if (bottomSheetActivityDelegate != null) {
            bottomSheetActivityDelegate.onSaveInstanceState(outState);
        }
    }

    void onRestoreInstanceState(Bundle savedInstanceState) {
        if (bottomSheetActivityDelegate != null) {
            bottomSheetActivityDelegate.onRestoreInstanceState(savedInstanceState);
        }
    }

    void onResume() {
        criticalLogoutSubscription = App.getAppComponent(activity).rxBus()
                .getEvents(CriticalLogoutEvent.class)
                .compose(RxTransformers.androidIO())
                .subscribe(this::handleUnauthorisedEvent);
    }

    void showBottomSheet(Fragment fragment, String tag) {
        if (bottomSheetActivityDelegate != null) {
            bottomSheetActivityDelegate.showBottomSheet(fragment, tag);
        } else {
            throw new IllegalStateException("BottomSheetActivity delegate is null." +
                    " Did you provide bottom_sheet_fragment_container view for this activity?");
        }
    }

    FragmentTransaction replaceFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, fragment, tag);
        return ft;
    }

    void onPause() {
        criticalLogoutSubscription.unsubscribe();
    }

    boolean onBackPressed() {
        boolean handled = false;

        if (bottomSheetActivityDelegate != null && bottomSheetActivityDelegate.isBottomSheetOpen()) {
            bottomSheetActivityDelegate.hideBottomSheet();
            handled = true;
        }

        return handled;
    }

    protected void showTextOnSnackbar(@StringRes int stringRes) {
        Snackbar.make(contentView, stringRes, Snackbar.LENGTH_LONG).show();
    }

    protected void showTextOnSnackbar(String text) {
        Snackbar.make(contentView, text, Snackbar.LENGTH_LONG).show();
    }

    private void handleUnauthorisedEvent(CriticalLogoutEvent object) {
        LoginActivity.startActivityClearTaskWithMessage(activity, object.getReason());
        activity.finish();
    }


}
