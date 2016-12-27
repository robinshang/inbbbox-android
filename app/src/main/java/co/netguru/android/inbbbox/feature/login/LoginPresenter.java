package co.netguru.android.inbbbox.feature.login;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.session.controllers.TokenController;
import co.netguru.android.inbbbox.data.session.controllers.TokenParametersController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.app.usercomponent.UserModeType;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

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
    private int guestModeCounter = 0;

    @Inject
    LoginPresenter(TokenParametersController oauthUrlController,
                   TokenController apiTokenController,
                   ErrorController errorController,
                   UserController userController) {
        this.tokenParametersController = oauthUrlController;
        this.apiTokenController = apiTokenController;
        this.errorController = errorController;
        this.userController = userController;
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
                        .compose(androidIO())
                        .subscribe(
                                urlUUIDPair -> getView()
                                        .openAuthWebViewFragment(urlUUIDPair.first,
                                                urlUUIDPair.second.toString()),
                                throwable -> handleError(throwable, "Problem with authorization")));
    }

    @Override
    public void handleKeysNotMatching() {
        getView().showWrongKeyError();
    }

    @Override
    public void handleWebViewClose() {
        getView().enableLoginButton();
    }

    @Override
    public void handleOauthCodeReceived(@NonNull String receivedCode) {
        requestTokenAndLoadUserData(receivedCode);
    }

    @Override
    public void handleUnknownOauthError() {
        getView().showInvalidOauthUrlError();
    }

    @Override
    public void handleKnownOauthError(@NonNull String oauthErrorMessage) {
        getView().showMessageOnServerError(oauthErrorMessage);
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));

    }

    @Override
    public void checkGuestMode() {
        guestModeCounter++;
        if (guestModeCounter == GUEST_MODE_ACTIVATION_THRESHOLD) {
            getView().showGuestModeLoginButton();
        }
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
    }

    private void requestTokenAndLoadUserData(String code) {
        compositeSubscription.add(
                apiTokenController.requestNewToken(code)
                        .flatMap(token -> userController.requestUser())
                        .compose(androidIO())
                        .subscribe(user -> handleOnlineUserLogin(),
                                throwable -> handleError(throwable,
                                        "Error while requesting new token")));
    }

    private void handleOnlineUserLogin() {
        getView().initializeUserMode(UserModeType.ONLINE_USER_MODE);
        getView().showNextScreen();
    }
}
