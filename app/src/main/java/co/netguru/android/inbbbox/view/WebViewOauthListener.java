package co.netguru.android.inbbbox.view;

import android.net.Uri;

public interface WebViewOauthListener {

    void redirectUrlCallbackLoaded(Uri uri);

    void stateKeyNotMatching();

    void webViewClose();
}
