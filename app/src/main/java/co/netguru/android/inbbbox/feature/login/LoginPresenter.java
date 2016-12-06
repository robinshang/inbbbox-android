package co.netguru.android.inbbbox.feature.login;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.OauthUrlController;
import co.netguru.android.inbbbox.controler.TokenController;
import co.netguru.android.inbbbox.controler.UserController;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@ActivityScope
public final class LoginPresenter
        extends MvpNullObjectBasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private static final int GUEST_MODE_ACTIVATION_THRESHOLD = 5;

    private final OauthUrlController oauthUrlController;
    private final TokenController apiTokenController;
    private final ErrorMessageController errorHandler;
    private final UserController userController;
    private final CompositeSubscription compositeSubscription;
    private int guestModeCounter = 1;

    @Inject
    LoginPresenter(OauthUrlController oauthUrlController,
                   TokenController apiTokenController,
                   ErrorMessageController apiErrorParser,
                   UserController userController) {
        this.oauthUrlController = oauthUrlController;
        this.apiTokenController = apiTokenController;
        this.errorHandler = apiErrorParser;
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
                oauthUrlController.getOauthAuthorizeUrlAndUuidPair()
                        .compose(androidIO())
                        .subscribe(
                                urlUUIDPair -> getView().openAuthWebViewFragment(urlUUIDPair.first, urlUUIDPair.second.toString()),
                                this::handleError));
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
        getView().showApiError(oauthErrorMessage);
    }

    @Override
    public void checkGuestMode() {
        guestModeCounter++;
        if (guestModeCounter == GUEST_MODE_ACTIVATION_THRESHOLD) {
            getView().showGuestModeLoginButton();
        }
    }

    private void requestTokenAndLoadUserData(String code) {
        compositeSubscription.add(
                apiTokenController.requestNewToken(code)
                        .flatMap(token -> userController.requestUser())
                        .compose(androidIO())
                        .subscribe(user -> getView().showNextScreen(),
                                this::handleError));
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable, "Error while getting user");
        getView().showApiError(errorHandler.getErrorMessageLabel(throwable));
    }

}
