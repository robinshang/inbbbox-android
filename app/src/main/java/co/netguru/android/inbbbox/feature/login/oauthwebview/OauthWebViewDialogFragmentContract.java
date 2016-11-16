package co.netguru.android.inbbbox.feature.login.oauthwebview;


import android.net.Uri;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface OauthWebViewDialogFragmentContract {
    interface View extends MvpView {

        void loadUrl(String url);

        void finishWithError(String oauthErrorMessage);

        void finishWithCodeReturn(String receivedCode);

        void finishWithStateKeyNotMatchingError();

        void finishWithUnknownError();
    }

    interface Presenter extends MvpPresenter<View> {

        boolean shouldOverrideUrlLoading(Uri uri);

        void handleData(String url, String stateKey);
    }
}
