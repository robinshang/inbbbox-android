package co.netguru.android.inbbbox.feature.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import co.netguru.android.inbbbox.feature.main.MainActivity;

public class SplashActivity extends MvpActivity<SplashContract.View, SplashContract.Presenter>
        implements SplashContract.View {

    public static final int SPLASH_ACTIVITY_REQUEST_CODE = 1;
    private SplashScreenComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initComponent();
        super.onCreate(savedInstanceState);
        getPresenter().initializeDefaultNightMode();
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
    public void setDefaultNightMode(boolean isNightMode) {
        AppCompatDelegate.setDefaultNightMode(isNightMode
                ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        Toast.makeText(this, errorText, Toast.LENGTH_LONG).show();
    }
}
