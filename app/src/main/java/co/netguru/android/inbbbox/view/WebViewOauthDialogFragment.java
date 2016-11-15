package co.netguru.android.inbbbox.view;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.R;
import timber.log.Timber;

public class WebViewOauthDialogFragment extends DialogFragment {

    public static final String TAG = WebViewOauthDialogFragment.class.getSimpleName();
    private static final String ARG_URL = "arg_url";
    private static final String ARG_STATE_KEY = "arg_state__key";

    private WebViewOauthListener callback;
    private String stateKey;
    private FocusableWebView webView;

    public static WebViewOauthDialogFragment newInstance(String url, String stateKey) {
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_STATE_KEY, stateKey);
        WebViewOauthDialogFragment dialogFragment = new WebViewOauthDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (WebViewOauthListener) context;
        } catch (ClassCastException e) {
            Timber.e(e.getMessage());
            throw new ClassCastException(context.toString()
                    + " must implement WebViewOauthListener");
        }
        this.stateKey = getArguments().getString(ARG_STATE_KEY);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        webView = new FocusableWebView(getActivity());

        dialogBuilder.setView(webView);

        webView.setWebViewClient(getWebViewClient());
        webView.loadUrl(getArguments().getString(ARG_URL));
        webView.getSettings().setUseWideViewPort(true);

        return dialogBuilder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webView != null) {
            webView.destroy();
        }
        callback.webViewClose();
    }

    @NonNull
    private WebViewClient getWebViewClient() {
        return new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.startsWith(getString(R.string.redirectUriScheme))) {
                    Uri uri = Uri.parse(url);
                    if (uri.getQueryParameter(Constants.OAUTH.STATE_KEY).equals(stateKey)) {
                        callback.redirectUrlCallbackLoaded(uri);
                    } else {
                        callback.stateKeyNotMatching();
                    }
                    dismiss();
                }
            }
        };
    }
}