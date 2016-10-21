package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.feature.authentication.ApiTokenProvider;
import co.netguru.android.inbbbox.feature.authentication.OauthUriProvider;
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

    private OauthUriProvider uriProvider;
    private ApiTokenProvider apiTokenProvider;
    private ErrorMessageParser errorHandler;
    private UserProvider userProvider;

    private String code;
    private String oauthErrorMessage;
    private String currentState;

    @Inject
    LoginPresenter(OauthUriProvider oauthUriProvider,
                   ApiTokenProvider apiTokenProvider,
                   ErrorMessageParser apiErrorParser,
                   UserProvider userProvider) {
        this.uriProvider = oauthUriProvider;
        this.apiTokenProvider = apiTokenProvider;
        this.errorHandler = apiErrorParser;
        this.userProvider = userProvider;
    }

    @Override
    public void showLoginView() {
        uriProvider
                .getOauthAuthorizeUriString()
                .doOnError(Throwable::printStackTrace)
                .doOnNext(this::prepareAuthorization)
                .subscribe();
    }

    private void prepareAuthorization(String uriString) {
        getView().handleOauthUri(uriString);
    }

    @Override
    public void handleOauthLoginResponse(Uri uri) {
        if (uri != null) {
            Timber.d(uri.toString());
            getView().closeLoginDialog();
            unpackParamsFromUri(uri);
            selectAuthorizationAction();
        } else {
            Timber.d("Uri is null");
        }
    }

    private void selectAuthorizationAction() {
        if (code != null && !code.isEmpty()) {
            getToken();
        } else if (oauthErrorMessage != null && !oauthErrorMessage.isEmpty()) {
            getView().showApiError(oauthErrorMessage);
        } else {
            getView().showApiError(errorHandler.getErrorLabel(ErrorType.INVALID_OAURH_URI));
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
        currentState = uri.getQueryParameter(Constants.OAUTH.STATE_KEY);
        oauthErrorMessage = uri.getQueryParameter(Constants.OAUTH.ERROR_KEY);
    }

}
