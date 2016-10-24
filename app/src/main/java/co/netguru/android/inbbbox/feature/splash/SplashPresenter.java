package co.netguru.android.inbbbox.feature.splash;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.feature.authentication.TokenProvider;
import co.netguru.android.inbbbox.feature.authentication.UserProvider;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class SplashPresenter extends MvpNullObjectBasePresenter<SplashContract.View>
        implements SplashContract.Presenter {

    private final TokenProvider tokenProvider;
    private final UserProvider userProvider;
    private Boolean isValid = false;

    @Inject
    SplashPresenter(TokenProvider tokenProvider, UserProvider userProvider) {

        this.tokenProvider = tokenProvider;
        this.userProvider = userProvider;
    }

    @Override
    public void attachView(SplashContract.View view) {
        super.attachView(view);
        checkToken();
    }

    private void checkToken() {
        tokenProvider.isTokenValid()
                .compose(androidIO())
                .doOnNext(isValid1 -> SplashPresenter.this.isValid = isValid1)
                .doOnCompleted(this::handleTokenVerificationResult)
                .subscribe();
    }

    private void handleTokenVerificationResult() {
        if (isValid) {
            Timber.d("Token valid");
            getCurrentUserInstance();
        } else {
            Timber.d("Token invalid");
            getView().showLoginScreen();
        }
    }

    private void getCurrentUserInstance() {
        userProvider.getUser()
                .compose(androidIO())
                .doOnError(this::onError)
                .doOnNext(this::handleUserDownloadComplete)
                .subscribe();

    }

    private void handleUserDownloadComplete(Boolean isValid) {
        if (isValid) {
            Timber.d("User is valid");
            getView().showMainScreen();
        } else {
            Timber.d("User saving failed");
            getView().showLoginScreen();
        }
    }

    private void onError(Throwable throwable) {
        Timber.e(throwable.getMessage());
        getView().showMainScreen();
    }
}
