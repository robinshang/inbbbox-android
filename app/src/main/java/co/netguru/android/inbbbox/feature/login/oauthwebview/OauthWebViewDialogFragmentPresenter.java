package co.netguru.android.inbbbox.feature.login.oauthwebview;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.app.usercomponent.UserModeType;
import co.netguru.android.inbbbox.common.analytics.AnalyticsEventLogger;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.StringUtil;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.session.controllers.TokenController;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class OauthWebViewDialogFragmentPresenter extends MvpNullObjectBasePresenter<OauthWebViewDialogFragmentContract.View>
        implements OauthWebViewDialogFragmentContract.Presenter {

    private String stateKey;
    private final TokenController apiTokenController;
    private final ErrorController errorController;
    private final UserController userController;
    private final AnalyticsEventLogger analyticsEventLogger;
    private final CompositeSubscription compositeSubscription;

    @Inject
    OauthWebViewDialogFragmentPresenter(TokenController apiTokenController,
                                        ErrorController errorController,
                                        UserController userController,
                                        AnalyticsEventLogger analyticsEventLogger) {
        this.apiTokenController = apiTokenController;
        this.errorController = errorController;
        this.userController = userController;
        this.analyticsEventLogger = analyticsEventLogger;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void handleData(String url, String stateKey) {
        this.stateKey = stateKey;
        getView().loadUrl(url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(Uri uri) {
        if (BuildConfig.DRIBBBLE_OAUTH_REDIRECT.equals(uri.getScheme())) {
            handleRedirectUri(uri);
            return true;
        }
        return false;
    }

    private void handleRedirectUri(Uri uri) {
        String oauthErrorMessage = uri.getQueryParameter(Constants.OAUTH.ERROR_KEY);
        if (!StringUtil.isBlank(oauthErrorMessage)) {
            getView().finishWithError(oauthErrorMessage);
        } else {
            handleRedirectUriWithoutError(uri);
        }
    }

    private void handleRedirectUriWithoutError(Uri uri) {
        String receivedStateKey = uri.getQueryParameter(Constants.OAUTH.STATE_KEY);
        if (stateKey.equals(receivedStateKey)) {
            handleUriWithProperStateKey(uri);
        } else {
            getView().finishWithStateKeyNotMatchingError();
        }
    }

    private void handleUriWithProperStateKey(Uri uri) {
        String receivedCode = uri.getQueryParameter(Constants.OAUTH.CODE_KEY);
        if (!StringUtil.isBlank(receivedCode)) {
            getView().finishWithCodeReturn(receivedCode);
        } else {
            getView().finishWithUnknownError();
        }
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
        analyticsEventLogger.logEventLoginFail();
    }

    @Override
    public void handleKeysNotMatching() {
        getView().showWrongKeyError();
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

    private void requestTokenAndLoadUserData(String code) {
        compositeSubscription.add(
                apiTokenController.requestNewToken(code)
                        .flatMap(token -> userController.requestUser())
                        .compose(androidIO())
                        .toCompletable()
                        .andThen(userController.disableGuestMode())
                        .subscribe(this::handleOnlineUserLogin,
                                throwable -> handleError(throwable,
                                        "Error while requesting new token")));
    }

    @SuppressWarnings("unused")
    private void handleOnlineUserLogin() {
        getView().initializeUserMode(UserModeType.ONLINE_USER_MODE);
        getView().showNextScreen();
        getView().finish();
        analyticsEventLogger.logEventLoginSuccess();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        compositeSubscription.clear();
    }
}
