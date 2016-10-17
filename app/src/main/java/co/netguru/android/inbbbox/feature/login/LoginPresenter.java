package co.netguru.android.inbbbox.feature.login;

import android.net.Uri;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.ActivityScope;
import co.netguru.android.inbbbox.data.models.User;
import co.netguru.android.inbbbox.feature.authentication.ApiTokenProvider;
import co.netguru.android.inbbbox.feature.authentication.OauthUriProvider;
import co.netguru.android.inbbbox.feature.authentication.UserProvider;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorMessageParser;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorType;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Subscriber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@ActivityScope
public class LoginPresenter
        extends MvpBasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private OauthUriProvider uriProvider;
    private ApiTokenProvider apiTokenProvider;
    private ErrorMessageParser errorHandler;
    private UserProvider userProvider;

    private String code;
    private String oauthErrorMessage;
    private String currentState;

    @Inject
    public LoginPresenter(OauthUriProvider oauthUriProvider,
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
                .getOauthAutorizeUri()
                .doOnError(Throwable::printStackTrace)
                .doOnNext(uri -> prepareAuthorization(uri))
                .subscribe();
    }

    private void prepareAuthorization(Uri uri) {
        getView().sendActionIntent(uri);
    }

    @Override
    public void handleOauthLoginResponse(Uri uri) {
        if (uri != null) {
            unpackParamsFromUri(uri);
            selectAuthorizationAction();
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
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleError(e);
                    }

                    @Override
                    public void onNext(User user) {
                        verifyUser(user);
                    }
                });
    }

    private void verifyUser(User user) {
        if (user != null) {
            showMainScreen();
        } else {
            getView().showApiError(errorHandler.getErrorLabel(ErrorType.INVALID_USER_INSTANCE));
        }
    }


    private void showMainScreen() {
        getView().showNextScreen();
    }

    private void handleError(Throwable throwable) {
        getView().showApiError(errorHandler.getError(throwable));
    }

    private void unpackParamsFromUri(Uri uri) {
        code = uri.getQueryParameter(Constants.OAUTH.CODE_KEY);
        currentState = uri.getQueryParameter(Constants.OAUTH.STATE_KEY);
        oauthErrorMessage = uri.getQueryParameter(Constants.OAUTH.ERROR_KEY);
    }

}
