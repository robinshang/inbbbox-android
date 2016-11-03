package co.netguru.android.inbbbox.feature.splash;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.feature.authentication.TokenProvider;
import co.netguru.android.inbbbox.feature.authentication.UserProvider;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorMessageParser;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class SplashPresenter extends MvpNullObjectBasePresenter<SplashContract.View>
        implements SplashContract.Presenter {

    private final TokenProvider tokenProvider;
    private final UserProvider userProvider;
    private ErrorMessageParser errorParser;

    @Inject
    SplashPresenter(TokenProvider tokenProvider,
                    UserProvider userProvider,
                    ErrorMessageParser errorParser) {

        this.tokenProvider = tokenProvider;
        this.userProvider = userProvider;
        this.errorParser = errorParser;
    }

    @Override
    public void attachView(SplashContract.View view) {
        super.attachView(view);
        checkToken();
    }

    private void checkToken() {
        tokenProvider.isTokenValid()
                .compose(androidIO())
                .subscribe(this::handleTokenVerificationResult, this::handleError);
    }

    private void handleTokenVerificationResult(boolean isValid) {
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
                .subscribe(
                        this::handleUserDownloadComplete,
                        this::handleError);
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

    private void handleError(Throwable throwable) {
        Timber.e(throwable.getMessage());
        getView().showError(errorParser.getError(throwable));
        getView().showLoginScreen();
    }
}
