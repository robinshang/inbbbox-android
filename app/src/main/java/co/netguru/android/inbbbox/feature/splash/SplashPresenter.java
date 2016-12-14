package co.netguru.android.inbbbox.feature.splash;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.ErrorController;
import co.netguru.android.inbbbox.controler.TokenController;
import co.netguru.android.inbbbox.controler.UserController;
import rx.Single;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.inbbbox.utils.RxTransformerUtils.applySingleIoSchedulers;

public class SplashPresenter extends MvpNullObjectBasePresenter<SplashContract.View>
        implements SplashContract.Presenter {

    private final TokenController tokenController;
    private final UserController userController;
    private ErrorController errorController;
    private CompositeSubscription compositeSubscription;

    @Inject
    SplashPresenter(TokenController tokenController,
                    UserController userController,
                    ErrorController errorController) {

        this.tokenController = tokenController;
        this.userController = userController;
        this.errorController = errorController;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void attachView(SplashContract.View view) {
        super.attachView(view);
        checkToken();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        compositeSubscription.unsubscribe();
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
        getView().showLoginScreen();
    }

    private void checkToken() {
        compositeSubscription.add(
                getTokenValidationSingle()
                        .compose(applySingleIoSchedulers())
                        .subscribe(this::handleTokenVerificationResult,
                                throwable -> handleError(throwable, "Error while token validation"))
        );
    }

    private Single<Boolean> getTokenValidationSingle() {
        return Single.zip(tokenController.isTokenValid(),
                userController.isGuestModeEnabled(),
                (isTokenValid, isGuestModeEnabled) -> isTokenValid && !isGuestModeEnabled);
    }

    private void handleTokenVerificationResult(Boolean isValid) {
        if (isValid) {
            Timber.d("Token valid");
            getCurrentUserInstance();
        } else {
            Timber.d("Token invalid");
            getView().showLoginScreen();
        }
    }

    private void getCurrentUserInstance() {
        compositeSubscription.add(
                userController.requestUser()
                        .compose(androidIO())
                        .subscribe(user -> handleLoggedUser(),
                                throwable -> handleError(throwable, "Error while requesting user"))
        );
    }

    private void handleLoggedUser() {
        getView().initializeOnlineUserMode();
        getView().showMainScreen();
    }
}