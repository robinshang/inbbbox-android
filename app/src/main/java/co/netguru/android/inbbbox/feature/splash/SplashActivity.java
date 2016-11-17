package co.netguru.android.inbbbox.feature.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.di.component.SplashScreenComponent;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import co.netguru.android.inbbbox.feature.main.MainActivity;

public class SplashActivity extends MvpActivity<SplashContract.View, SplashContract.Presenter>
        implements SplashContract.View {

    private SplashScreenComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initCrashManager();
        initComponent();
        super.onCreate(savedInstanceState);
    }

    private void initCrashManager() {
        CrashManager.register(this, BuildConfig.HOCKEY_APP_ID, new CrashManagerListener() {
            @Override
            public boolean shouldAutoUploadCrashes() {
                return true;
            }
        });
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
        LoginActivity.startActivity(this, message);
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
