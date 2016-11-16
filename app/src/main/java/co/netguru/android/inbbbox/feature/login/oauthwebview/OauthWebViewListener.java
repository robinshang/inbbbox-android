package co.netguru.android.inbbbox.feature.login.oauthwebview;

import android.support.annotation.NonNull;

public interface OauthWebViewListener {

    void onOauthStateKeyNotMatching();

    void onOauthCodeReceive(@NonNull String receivedCode);

    void onOauthUnknownError();

    void onOauthKnownError(@NonNull String oauthErrorMessage);

    void onOauthFragmentClose();
}