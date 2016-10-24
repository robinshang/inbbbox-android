package co.netguru.android.inbbbox.feature.splash;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.feature.authentication.TokenProvider;
import co.netguru.android.inbbbox.feature.authentication.UserProvider;

public class SplashPresenter extends MvpNullObjectBasePresenter<SplashContract.View>
        implements SplashContract.Presenter {

    private TokenProvider tokenProvider;

    @Inject
    SplashPresenter(TokenProvider tokenProvider, UserProvider userProvider) {

        this.tokenProvider = tokenProvider;
    }

    @Override
    public void attachView(SplashContract.View view) {

    }

    @Override
    public void detachView(boolean retainInstance) {

    }
}
