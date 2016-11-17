package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.Constants;
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

    private final OauthUrlController oauthUrlController;
    private final TokenController apiTokenController;
    private final ErrorMessageController errorHandler;
    private final UserController userController;
    private final CompositeSubscription compositeSubscription;

    private String code;
    private String oauthErrorMessage;

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
    public void attachView(LoginContract.View view) {
        super.attachView(view);
        String initialMessage = getView().getInitialMessage();
        if (initialMessage != null && !initialMessage.isEmpty()) {
            getView().showInitialMessage(initialMessage);
        }
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        compositeSubscription.clear();
    }

    @Override
    public void showLoginView() {
        compositeSubscription.add(
                oauthUrlController.getOauthAuthorizeUrlString()
                        .compose(androidIO())
                        .subscribe(
                                getView()::handleOauthUrl,
                                this::handleError));
    }

    @Override
    public void handleOauthLoginResponse(Uri url) {
        if (url != null) {
            Timber.d(url.toString());
            getView().closeLoginDialog();
            unpackParamsFromUri(url);
            selectAuthorizationAction();
        } else {
            Timber.d("url is null");
        }
    }

    private void selectAuthorizationAction() {
        if (code != null && !code.isEmpty()) {
            requestNewToken();
        } else if (oauthErrorMessage != null && !oauthErrorMessage.isEmpty()) {
            getView().showApiError(oauthErrorMessage);
        } else {
            getView().showInvalidOauthUrlError();
        }
    }

    private void requestNewToken() {
        compositeSubscription.add(
                apiTokenController.requestNewToken(code)
                        .compose(androidIO())
                        .subscribe(token -> getUser(),
                                this::handleError
                        ));
    }

    private void getUser() {
        compositeSubscription.add(
                userController.requestUser()
                        .compose(androidIO())
                        .subscribe(user -> getView().showNextScreen(),
                                this::handleError));
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable, "Error while getting user");
        getView().showApiError(errorHandler.getError(throwable));
    }

    private void unpackParamsFromUri(Uri uri) {
        code = uri.getQueryParameter(Constants.OAUTH.CODE_KEY);
        oauthErrorMessage = uri.getQueryParameter(Constants.OAUTH.ERROR_KEY);
    }

}
