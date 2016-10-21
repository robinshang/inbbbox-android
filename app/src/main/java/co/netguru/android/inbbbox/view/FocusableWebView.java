package co.netguru.android.inbbbox.view;

import android.content.Context;
import android.webkit.WebView;

//    Touch focus handling because of WebView issue
//    https://code.google.com/p/android/issues/detail?id=7189
public class FocusableWebView extends WebView {

    public FocusableWebView(Context context) {
        super(context);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }
}