package co.netguru.android.inbbbox.feature.login;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import co.netguru.android.commons.di.WithComponent;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.application.App;
import co.netguru.android.inbbbox.feature.main.MainActivity;

public class LoginActivity extends MvpActivity<LoginContract.View, LoginContract.Presenter>
        implements LoginContract.View, WithComponent<LoginComponent> {

    @OnClick(R.id.btn_login)
    void onLoginClick() {
        getPresenter().showLoginView();
    }

    private LoginComponent component;

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
        Uri uri = getIntent().getData();
        getPresenter().handleOauthLoginResponse(uri);
    }

    @Override
    public void sendActionIntent(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void showApiError(String oauthErrorMessage) {
        Toast.makeText(this, oauthErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNextScreen() {
        MainActivity.startActivity(this);
    }
}
