package co.netguru.android.inbbbox.feature.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import co.netguru.android.commons.di.WithComponent;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.di.component.LoginComponent;
import co.netguru.android.inbbbox.di.module.LoginModule;
import co.netguru.android.inbbbox.feature.main.MainActivity;
import co.netguru.android.inbbbox.view.OnRedirectUrlCallbackListener;
import co.netguru.android.inbbbox.view.WebviewDialogFragment;

public class LoginActivity extends MvpActivity<LoginContract.View, LoginContract.Presenter>
        implements LoginContract.View, WithComponent<LoginComponent>,
        OnRedirectUrlCallbackListener {

    private WebviewDialogFragment dialogFragment;
    private LoginComponent component;

    @OnClick(R.id.btn_login)
    void onLoginClick() {
        getPresenter().showLoginView();
        net.hockeyapp.android.LoginActivity.class.getSimpleName();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void handleOauthUrl(String url) {
        dialogFragment = WebviewDialogFragment.newInstance(url);
        dialogFragment.show(getFragmentManager(), WebviewDialogFragment.TAG);
    }

    @Override
    public void showApiError(String oauthErrorMessage) {
        Toast.makeText(this, oauthErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNextScreen() {
        MainActivity.startActivity(this);
        finish();
    }

    @Override
    public void closeLoginDialog() {
        dialogFragment.dismiss();
    }

    @Override
    public void redirectUrlCallbackLoaded(String url) {
        getPresenter().handleOauthLoginResponse(Uri.parse(url));
    }
}
