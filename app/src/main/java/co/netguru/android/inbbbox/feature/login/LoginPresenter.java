package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.feature.authentication.OauthUrlProvider;
import co.netguru.android.inbbbox.feature.authentication.TokenProvider;
import co.netguru.android.inbbbox.feature.authentication.UserProvider;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorMessageParser;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorType;
import co.netguru.android.inbbbox.utils.Constants;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@ActivityScope
public final class LoginPresenter
        extends MvpNullObjectBasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private final OauthUrlProvider urlProvider;
    private final TokenProvider apiTokenProvider;
    private final ErrorMessageParser errorHandler;
    private final UserProvider userProvider;
    private final CompositeSubscription compositeSubscription;

    private String code;
    private String oauthErrorMessage;

    @Inject
    LoginPresenter(OauthUrlProvider oauthUrlProvider,
                   TokenProvider apiTokenProvider,
                   ErrorMessageParser apiErrorParser,
                   UserProvider userProvider) {
        this.urlProvider = oauthUrlProvider;
        this.apiTokenProvider = apiTokenProvider;
        this.errorHandler = apiErrorParser;
        this.userProvider = userProvider;
        compositeSubscription = new CompositeSubscription();

    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        compositeSubscription.clear();
    }

    @Override
    public void showLoginView() {
        compositeSubscription.add(
                urlProvider.getOauthAuthorizeUrlString()
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
            getView().showApiError(errorHandler.getErrorLabel(ErrorType.INVALID_OAUTH_URL));
        }
    }

    private void requestNewToken() {
        compositeSubscription.add(apiTokenProvider.getToken(code)
                .compose(androidIO())
                .subscribe(saved -> {
                            if (saved) {
                                getUser();
                            }
                        },
                        this::handleError
                ));
    }

    private void getUser() {
        compositeSubscription.add(
                userProvider.getUser()
                        .compose(androidIO())
                        .subscribe(this::verifyUser, this::handleError));
    }

    private void verifyUser(Boolean status) {
        if (status) {
            getView().showNextScreen();
        } else {
            getView().showApiError(errorHandler.getErrorLabel(ErrorType.INVALID_USER_INSTANCE));
        }
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
