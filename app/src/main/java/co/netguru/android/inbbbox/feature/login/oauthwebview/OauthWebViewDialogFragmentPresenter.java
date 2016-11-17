package co.netguru.android.inbbbox.feature.login.oauthwebview;


import android.net.Uri;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.utils.StringUtils;

public class OauthWebViewDialogFragmentPresenter extends MvpNullObjectBasePresenter<OauthWebViewDialogFragmentContract.View>
        implements OauthWebViewDialogFragmentContract.Presenter {

    private String stateKey;

    @Inject
    OauthWebViewDialogFragmentPresenter() {
        //DI
    }

    @Override
    public void handleData(String url, String stateKey) {
        this.stateKey = stateKey;
        getView().loadUrl(url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(Uri uri) {
        String url = uri.toString();
        if (url.startsWith(BuildConfig.DRIBBBLE_OAUTH_REDIRECT)) {
            handleRedirectUri(uri);
            return true;
        }
        return false;
    }

    private void handleRedirectUri(Uri uri) {
        String oauthErrorMessage = uri.getQueryParameter(Constants.OAUTH.ERROR_KEY);
        if (!StringUtils.isBlank(oauthErrorMessage)) {
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
        if (!StringUtils.isBlank(receivedCode)) {
            getView().finishWithCodeReturn(receivedCode);
        } else {
            getView().finishWithUnknownError();
        }
    }
}
