package co.netguru.android.inbbbox.feature.splash;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.TokenController;
import co.netguru.android.inbbbox.controler.UserController;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class SplashPresenter extends MvpNullObjectBasePresenter<SplashContract.View>
        implements SplashContract.Presenter {

    private final TokenController tokenController;
    private final UserController userController;
    private ErrorMessageController errorParser;

    @Inject
    SplashPresenter(TokenController tokenController,
                    UserController userController,
                    ErrorMessageController errorParser) {

        this.tokenController = tokenController;
        this.userController = userController;
        this.errorParser = errorParser;
    }

    @Override
    public void attachView(SplashContract.View view) {
        super.attachView(view);
        checkToken();
    }

    private void checkToken() {
        tokenController.isTokenValid()
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
        userController.requestUser()
                .compose(androidIO())
                .subscribe(user -> getView().showMainScreen(),
                        this::handleError);
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable.getMessage());
        getView().showError(errorParser.getError(throwable));
        getView().showLoginScreen();
    }
}
