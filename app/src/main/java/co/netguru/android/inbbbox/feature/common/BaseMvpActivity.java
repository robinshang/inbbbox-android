package co.netguru.android.inbbbox.feature.common;

import android.support.annotation.LayoutRes;
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
import co.netguru.android.inbbbox.event.CriticalLogoutEvent;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import rx.Subscription;

public abstract class BaseMvpActivity<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpActivity<V, P> {

    @BindView(android.R.id.content)
    View contentView;
    private Subscription criticalLogoutSubscription;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
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

    protected FragmentTransaction replaceFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerViewId, fragment, tag);
        return ft;
    }

    protected void showTextOnSnackbar(@StringRes int stringRes) {
        Snackbar.make(contentView, stringRes, Snackbar.LENGTH_LONG).show();
    }

    private void handleUnauthorisedEvent(CriticalLogoutEvent object) {
        Toast.makeText(this, object.getReason(), Toast.LENGTH_SHORT).show();
        LoginActivity.startActivityClearTask(this);
        finish();
    }
}