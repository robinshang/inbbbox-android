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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.netguru.android.commons.di.WithComponent;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.app.usercomponent.UserModeType;
import co.netguru.android.inbbbox.common.utils.AnimationUtil;
import co.netguru.android.inbbbox.feature.login.oauthwebview.OauthWebViewDialogFragment;
import co.netguru.android.inbbbox.feature.login.oauthwebview.OauthWebViewListener;
import co.netguru.android.inbbbox.feature.main.MainActivity;

public class LoginActivity extends MvpActivity<LoginContract.View, LoginContract.Presenter>
        implements LoginContract.View, WithComponent<LoginComponent>,
        OauthWebViewListener {

    private static final int SLIDE_IN_DURATION = 300;
    private LoginComponent component;

    @BindView(R.id.btn_login)
    Button loginButton;

    @BindView(R.id.guest_btn_divider)
    View guestModeDivider;

    @BindView(R.id.btn_guest)
    Button guestButton;

    @BindView(R.id.login_relative_layout)
    RelativeLayout loginRelativeLayout;

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

    public static void startActivityClearTask(Context context) {
        final Intent intent = new Intent(context, LoginActivity.class);
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
    public void initializeUserMode(UserModeType mode) {
        App.initUserComponent(this, mode);
    }

    @Override
    public void showNextScreen() {
        MainActivity.startActivity(this);
        finish();
    }

    @Override
    public void onOauthStateKeyNotMatching() {
        getPresenter().handleKeysNotMatching();
    }

    @Override
    public void onOauthCodeReceive(@NonNull String receivedCode) {
        getPresenter().handleOauthCodeReceived(receivedCode);
    }

    @Override
    public void onOauthUnknownError() {
        getPresenter().handleUnknownOauthError();
    }

    @Override
    public void onOauthKnownError(@NonNull String oauthErrorMessage) {
        getPresenter().handleKnownOauthError(oauthErrorMessage);
    }

    @Override
    public void onOauthFragmentClose() {
        getPresenter().handleWebViewClose();
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        showMessageOnSnackBar(errorText);
    }

    private void showMessageOnSnackBar(String message) {
        Snackbar.make(loginRelativeLayout, message, Snackbar.LENGTH_LONG).show();
    }
}