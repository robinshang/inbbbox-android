package co.netguru.android.inbbbox.feature.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.netguru.android.commons.di.WithComponent;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.LoginComponent;
import co.netguru.android.inbbbox.di.module.LoginModule;
import co.netguru.android.inbbbox.feature.login.oauthwebview.OauthWebViewDialogFragment;
import co.netguru.android.inbbbox.feature.login.oauthwebview.OauthWebViewListener;
import co.netguru.android.inbbbox.feature.main.MainActivity;

public class LoginActivity extends MvpActivity<LoginContract.View, LoginContract.Presenter>
        implements LoginContract.View, WithComponent<LoginComponent>,
        OauthWebViewListener {

    private LoginComponent component;

    @BindView(R.id.btn_login)
    Button loginButton;

    @OnClick(R.id.btn_login)
    void onLoginClick() {
        getPresenter().showLoginView();
    }

    public static void startActivity(Context context) {
        final Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initComponent();
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
    public void showApiError(String oauthErrorMessage) {
        Toast.makeText(this, oauthErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showInvalidOauthUrlError() {
        Toast.makeText(this, R.string.invalid_outh_url, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showWrongKeyError() {
        Toast.makeText(this, R.string.wrong_oauth_state_key, Toast.LENGTH_LONG).show();
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
}