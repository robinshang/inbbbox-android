package co.netguru.android.inbbbox.feature.login;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.app.usercomponent.UserModeType;
import co.netguru.android.inbbbox.common.analytics.AnalyticsEventLogger;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.session.controllers.TokenController;
import co.netguru.android.inbbbox.data.session.controllers.TokenParametersController;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

@ActivityScope
public final class LoginPresenter
        extends MvpNullObjectBasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private static final int GUEST_MODE_ACTIVATION_THRESHOLD = 5;

    private final TokenParametersController tokenParametersController;
    private final TokenController apiTokenController;
    private final ErrorController errorController;
    private final UserController userController;
    private final CompositeSubscription compositeSubscription;
    private final AnalyticsEventLogger analyticsEventLogger;
    private int guestModeCounter = 0;

    @Inject
    LoginPresenter(TokenParametersController oauthUrlController,
                   TokenController apiTokenController,
                   ErrorController errorController,
                   UserController userController,
                   AnalyticsEventLogger analyticsEventLogger) {
        this.tokenParametersController = oauthUrlController;
        this.apiTokenController = apiTokenController;
        this.errorController = errorController;
        this.userController = userController;
        this.analyticsEventLogger = analyticsEventLogger;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        compositeSubscription.clear();
    }

    @Override
    public void showLoginView() {
        getView().disableLoginButton();
        compositeSubscription.add(
                tokenParametersController.getOauthAuthorizeUrlAndUuidPair()
                        .compose(RxTransformerUtil.androidComputation())
                        .subscribe(
                                urlUUIDPair -> getView()
                                        .openAuthWebViewFragment(urlUUIDPair.first,
                                                urlUUIDPair.second.toString()),
                                throwable -> handleError(throwable, "Problem with authorization")));
    }

    @Override
    public void loginWithGuestClicked() {
        compositeSubscription.add(
                tokenParametersController.getUserGuestToken()
                        .flatMapCompletable(apiTokenController::saveToken)
                        .andThen(userController.enableGuestMode())
                        .subscribe(this::handleGuestLogin,
                                throwable -> handleError(throwable,
                                        "Error while getting user guest token")));
    }

    private void handleGuestLogin() {
        getView().initializeUserMode(UserModeType.GUEST_USER_MODE);
        getView().showNextScreen();
        analyticsEventLogger.logEventLoginGuest();
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
        analyticsEventLogger.logEventLoginFail();
    }

    @Override
    public void checkGuestMode() {
        guestModeCounter++;
        if (guestModeCounter == GUEST_MODE_ACTIVATION_THRESHOLD) {
            getView().showGuestModeLoginButton();
        }
    }

}
