package co.netguru.android.inbbbox.feature.common;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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
import co.netguru.android.inbbbox.feature.common.bottomsheet.BottomSheetActivityCallback;
import co.netguru.android.inbbbox.feature.common.bottomsheet.HidingBottomSheetActivityDelegate;
import co.netguru.android.inbbbox.feature.common.bottomsheet.HidingBottomSheetBearer;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import rx.Subscription;

public abstract class BaseMvpActivity<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpActivity<V, P> implements BottomSheetActivityCallback, HidingBottomSheetBearer {

    @BindView(android.R.id.content)
    View contentView;
    @Nullable
    @BindView(R.id.bottom_sheet_fragment_container)
    View bottomSheetView;

    @Nullable
    private HidingBottomSheetActivityDelegate bottomSheetActivityDelegate;

    private Subscription criticalLogoutSubscription;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (bottomSheetView != null) {
            bottomSheetActivityDelegate = new HidingBottomSheetActivityDelegate(this, bottomSheetView);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (bottomSheetActivityDelegate != null) {
            bottomSheetActivityDelegate.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (bottomSheetActivityDelegate != null) {
            bottomSheetActivityDelegate.onRestoreInstanceState(savedInstanceState);
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
        if (bottomSheetActivityDelegate != null && bottomSheetActivityDelegate.isBottomSheetOpen()) {
            bottomSheetActivityDelegate.hideBottomSheet();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public FragmentTransaction replaceFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, fragment, tag);
        return ft;
    }

    @Override
    public void removeFragmentFromView(@IdRes int viewId) {
        Fragment fragmentToRemove = getSupportFragmentManager().findFragmentById(viewId);
        if (fragmentToRemove != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragmentToRemove)
                    .commit();
        }
    }

    protected void showTextOnSnackbar(@StringRes int stringRes) {
        Snackbar.make(contentView, stringRes, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showBottomSheet(Fragment fragment, String tag) {
        if (bottomSheetActivityDelegate != null) {
            bottomSheetActivityDelegate.showBottomSheet(fragment, tag);
        } else {
            throw new IllegalStateException("BottomSheetActivity delegate is null." +
                    " Did you provide fragment_container view for this activity?");
        }
    }

    private void handleUnauthorisedEvent(CriticalLogoutEvent object) {
        Toast.makeText(this, object.getReason(), Toast.LENGTH_SHORT).show();
        LoginActivity.startActivityClearTask(this);
        finish();
    }
}