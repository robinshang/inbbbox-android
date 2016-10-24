package co.netguru.android.inbbbox.feature.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import co.netguru.android.inbbbox.feature.login.LoginActivity;

public class SplashActivity extends MvpActivity<SplashContract.View, SplashContract.Presenter>
        implements SplashContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @NonNull
    @Override
    public SplashContract.Presenter createPresenter() {
        return null;
    }
}
