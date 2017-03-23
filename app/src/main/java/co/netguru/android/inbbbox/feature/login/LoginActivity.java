package co.netguru.android.inbbbox.feature.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.netguru.android.commons.di.WithComponent;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.analytics.AnalyticsEventLogger;
import co.netguru.android.inbbbox.common.utils.AnimationUtil;
import co.netguru.android.inbbbox.feature.login.oauthwebview.OauthWebViewDialogFragment;
import co.netguru.android.inbbbox.feature.main.MainActivity;
import co.netguru.android.inbbbox.feature.shared.view.MultipleScrollingBackgroundsView;

public class LoginActivity extends MvpActivity<LoginContract.View, LoginContract.Presenter>
        implements LoginContract.View, WithComponent<LoginComponent> {

    private static final int SLIDE_IN_DURATION = 300;
    private static final String MESSAGE_KEY = "message_key";
    private LoginComponent component;

    @Inject
    AnalyticsEventLogger analyticsEventLogger;

    @BindView(R.id.btn_login)
    Button loginButton;

    @BindView(R.id.guest_btn_divider)
    View guestModeDivider;

    @BindView(R.id.btn_guest)
    Button guestButton;

    @BindView(R.id.login_relative_layout)
    RelativeLayout loginRelativeLayout;

    @BindView(R.id.scrolling_background)
    MultipleScrollingBackgroundsView scrollingBackgroundsView;

    @OnClick(R.id.btn_login)
    void onLoginClick() {
        getPresenter().showLoginView();
    }

    @OnClick(R.id.login_logo_ball)
    void onLogoClick() {
        getPresenter().checkGuestMode();
    }

    @OnClick(R.id.btn_guest)
    void onGuestLoginClick() {
        getPresenter().loginWithGuestClicked();
    }

    public static void startActivity(Context context) {
        final Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startActivityClearTaskWithMessage(Context context, String message) {
        final Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(MESSAGE_KEY, message);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initComponent();
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (getIntent().getStringExtra(MESSAGE_KEY) != null) {
            showMessageOnSnackBar(getIntent().getStringExtra(MESSAGE_KEY));
        }
        scrollingBackgroundsView.startAnimation();
        analyticsEventLogger.logEventScreenLogin();
    }

    private void initComponent() {
        this.component = App.getAppComponent(this)
                .plus(new LoginModule());
        component.inject(this);
    }

    @NonNull
    @Override
    public LoginContract.Presenter createPresenter() {
        return getComponent().getLoginPresenter();
    }

    @Override
    public LoginComponent getComponent() {
        return this.component;
    }

    @Override
    public void openAuthWebViewFragment(String url, String stateKey) {
        OauthWebViewDialogFragment.newInstance(url, stateKey)
                .show(getSupportFragmentManager(), OauthWebViewDialogFragment.TAG);
    }

    @Override
    public void showInvalidOauthUrlError() {
        showMessageOnSnackBar(getResources().getString(R.string.invalid_outh_url));
    }

    @Override
    public void showWrongKeyError() {
        showMessageOnSnackBar(getResources().getString(R.string.wrong_oauth_state_key));
    }

    @Override
    public void disableLoginButton() {
        loginButton.setEnabled(false);
    }

    @Override
    public void enableLoginButton() {
        loginButton.setEnabled(true);
    }

    @Override
    public void showGuestModeLoginButton() {
        AnimationUtil.startSlideInFromBottomShowAnimation(guestModeDivider, SLIDE_IN_DURATION);
        AnimationUtil.startSlideInFromBottomShowAnimation(guestButton, SLIDE_IN_DURATION);
    }

    @Override
    public void showNextScreen() {
        MainActivity.startActivity(this);
        finish();
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        showMessageOnSnackBar(errorText);
    }

    private void showMessageOnSnackBar(String message) {
        Snackbar.make(loginRelativeLayout, message, Snackbar.LENGTH_LONG).show();
    }
}