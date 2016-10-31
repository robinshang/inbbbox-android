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
import rx.Subscriber;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@ActivityScope
public final class LoginPresenter
        extends MvpNullObjectBasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private OauthUrlProvider urlProvider;
    private TokenProvider apiTokenProvider;
    private ErrorMessageParser errorHandler;
    private UserProvider userProvider;

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
    }

    @Override
    public void showLoginView() {
        urlProvider
                .getOauthAuthorizeUrlString()
                .doOnError(Throwable::printStackTrace)
                .doOnNext(this::prepareAuthorization)
                .subscribe();
    }

    private void prepareAuthorization(String urlString) {
        getView().handleOauthUrl(urlString);
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
            getToken();
        } else if (oauthErrorMessage != null && !oauthErrorMessage.isEmpty()) {
            getView().showApiError(oauthErrorMessage);
        } else {
            getView().showApiError(errorHandler.getErrorLabel(ErrorType.INVALID_OAURH_URL));
        }
    }

    private void getToken() {
        apiTokenProvider.getToken(code)
                .compose(androidIO())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleError(e);
                    }

                    @Override
                    public void onNext(Boolean saved) {
                        if (saved) {
                            getUser();
                        }
                    }
                });
    }

    private void getUser() {
        userProvider.getUser()
                .compose(androidIO())
                .subscribe(this::verifyUser, this::handleError);
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
