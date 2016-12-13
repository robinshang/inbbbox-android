package co.netguru.android.inbbbox.feature.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.di.component.SplashScreenComponent;
import co.netguru.android.inbbbox.enumeration.UserModeType;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import co.netguru.android.inbbbox.feature.main.MainActivity;

public class SplashActivity extends MvpActivity<SplashContract.View, SplashContract.Presenter>
        implements SplashContract.View {

    private SplashScreenComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initComponent();
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void initComponent() {
        this.component = App.getAppComponent(this)
                .plusSplashScreenComponent();
        component.inject(this);
    }

    @NonNull
    @Override
    public SplashContract.Presenter createPresenter() {
        return component.getSplashPresenter();
    }

    @Override
    public void showLoginScreen() {
        LoginActivity.startActivity(this);
        finish();
    }

    @Override
    public void showMainScreen() {
        MainActivity.startActivity(this);
        finish();
    }

    @Override
    public void initializeOnlineUserMode() {
        App.initUserComponent(this, UserModeType.ONLINE_USER_MODE);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        Toast.makeText(this, errorText, Toast.LENGTH_LONG).show();
    }
}
