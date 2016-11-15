package co.netguru.android.inbbbox.view;

import android.net.Uri;

public interface OauthWebViewListener {

    void redirectUrlCallbackLoaded(Uri uri);

    void stateKeyNotMatching();

    void webViewClose();
}