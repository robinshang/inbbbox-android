package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import co.netguru.android.commons.di.WithComponent;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.application.App;
import co.netguru.android.inbbbox.data.api.AuthorizeApiModule;

public class LoginActivity extends MvpActivity<LoginContract.View, LoginContract.Presenter>
        implements LoginContract.View, WithComponent<LoginComponent> {

    private LoginComponent component;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initComponent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
    public void sendActionIntent(Uri uri) {

    }

    @Override
    public void showApiError(String oauthErrorMessage) {

    }

    @Override
    public void showNextScreen() {

    }
}
