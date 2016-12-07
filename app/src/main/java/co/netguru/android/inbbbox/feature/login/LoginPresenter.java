package co.netguru.android.inbbbox.feature.login;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.controler.ErrorController;
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

    private final OauthUrlController oauthUrlController;
    private final TokenController apiTokenController;
    private final ErrorController errorController;
    private final UserController userController;
    private final CompositeSubscription compositeSubscription;

    @Inject
    LoginPresenter(OauthUrlController oauthUrlController,
                   TokenController apiTokenController,
                   ErrorController errorController,
                   UserController userController) {
        this.oauthUrlController = oauthUrlController;
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
                oauthUrlController.getOauthAuthorizeUrlAndUuidPair()
                        .compose(androidIO())
                        .subscribe(
                                urlUUIDPair -> getView().openAuthWebViewFragment(urlUUIDPair.first, urlUUIDPair.second.toString()),
                                throwable -> handleHttpErrorResponse(throwable, "Error while getting user")));
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
    public void handleHttpErrorResponse(Throwable throwable, String errorText) {
        Timber.e(throwable, "Error while getting user");
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));

    }

    private void requestTokenAndLoadUserData(String code) {
        compositeSubscription.add(
                apiTokenController.requestNewToken(code)
                        .flatMap(token -> userController.requestUser())
                        .compose(androidIO())
                        .subscribe(user -> getView().showNextScreen(),
                                throwable -> handleHttpErrorResponse(throwable, "Error while requesting new token")));
    }
}
