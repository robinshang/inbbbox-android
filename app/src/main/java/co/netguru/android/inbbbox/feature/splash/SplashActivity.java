package co.netguru.android.inbbbox.feature.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import net.hockeyapp.android.CrashManager;

import co.netguru.android.inbbbox.application.App;
import co.netguru.android.inbbbox.di.component.SplashScreenComponent;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import co.netguru.android.inbbbox.feature.main.MainActivity;

public class SplashActivity extends MvpActivity<SplashContract.View, SplashContract.Presenter>
        implements SplashContract.View {

    private SplashScreenComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CrashManager.register(this);
        initComponent();
        super.onCreate(savedInstanceState);
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
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
