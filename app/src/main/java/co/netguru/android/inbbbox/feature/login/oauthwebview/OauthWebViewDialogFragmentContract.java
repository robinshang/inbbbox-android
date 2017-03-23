package co.netguru.android.inbbbox.feature.login.oauthwebview;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

interface OauthWebViewDialogFragmentContract {
    interface View extends MvpView, HttpErrorView {

        void loadUrl(String url);

        void finishWithError(String oauthErrorMessage);

        void finishWithCodeReturn(String receivedCode);

        void finishWithStateKeyNotMatchingError();

        void finishWithUnknownError();

        void showNextScreen();

        void showInvalidOauthUrlError();

        void showWrongKeyError();

        void finish();
    }

    interface Presenter extends MvpPresenter<View> {

        boolean shouldOverrideUrlLoading(Uri uri);

        void handleData(String url, String stateKey);

        void handleError(Throwable throwable, String errorText);

        void handleKeysNotMatching();

        void handleOauthCodeReceived(@NonNull String receivedCode);

        void handleUnknownOauthError();

        void handleKnownOauthError(@NonNull String oauthErrorMessage);
    }
}
