package co.netguru.android.inbbbox.feature.splash;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.ErrorController;
import co.netguru.android.inbbbox.controler.TokenController;
import co.netguru.android.inbbbox.controler.UserController;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class SplashPresenter extends MvpNullObjectBasePresenter<SplashContract.View>
        implements SplashContract.Presenter {

    private final TokenController tokenController;
    private final UserController userController;
    private ErrorController errorController;

    @Inject
    SplashPresenter(TokenController tokenController,
                    UserController userController,
                    ErrorController errorController) {
        this.tokenController = tokenController;
        this.userController = userController;
        this.errorController = errorController;
    }

    @Override
    public void attachView(SplashContract.View view) {
        super.attachView(view);
        checkToken();
    }

    @Override
    public void handleHttpErrorResponse(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
        getView().showLoginScreen();
    }

    private void checkToken() {
        tokenController.isTokenValid()
                .compose(androidIO())
                .subscribe(this::handleTokenVerificationResult,
                        throwable -> handleHttpErrorResponse(throwable, "Error while token validation"));
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
                        throwable -> handleHttpErrorResponse(throwable, "Error while requesting user"));
    }
}
