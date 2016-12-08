package co.netguru.android.inbbbox.feature.splash;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.ErrorMessageController;
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
    private ErrorMessageController errorParser;
    private CompositeSubscription compositeSubscription;

    @Inject
    SplashPresenter(TokenController tokenController,
                    UserController userController,
                    ErrorMessageController errorParser) {

        this.tokenController = tokenController;
        this.userController = userController;
        this.errorParser = errorParser;
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

    private void checkToken() {
        compositeSubscription.add(
                getTokenValidationSingle()
                        .compose(applySingleIoSchedulers())
                        .subscribe(this::handleTokenVerificationResult, this::handleError)
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
                        .subscribe(user -> getView().showMainScreen(),
                                this::handleError)
        );
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable.getMessage());
        getView().showError(errorParser.getErrorMessageLabel(throwable));
        getView().showLoginScreen();
    }
}
