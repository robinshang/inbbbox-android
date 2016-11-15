package co.netguru.android.inbbbox.view;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import co.netguru.android.inbbbox.Constants;
import timber.log.Timber;

public class OauthWebViewDialogFragment extends DialogFragment {

    public static final String TAG = OauthWebViewDialogFragment.class.getSimpleName();
    private static final String ARG_URL = "arg_url";
    private static final String ARG_STATE_KEY = "arg_state__key";

    private OauthWebViewListener callback;
    private FocusableWebView webView;

    public static OauthWebViewDialogFragment newInstance(String url, String stateKey) {
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_STATE_KEY, stateKey);
        OauthWebViewDialogFragment dialogFragment = new OauthWebViewDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OauthWebViewListener) context;
        } catch (ClassCastException e) {
            Timber.e(e.getMessage());
            throw new ClassCastException(context.toString()
                    + " must implement OauthWebViewListener");
        }
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
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                final Uri uri = Uri.parse(url);
                return handleUri(uri);
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final Uri uri = request.getUrl();
                return handleUri(uri);
            }

            private boolean handleUri(final Uri uri) {
                if (uri.toString().startsWith(Constants.OAUTH.REDIRECT_URI_STRING)) {
                    if (uri.getQueryParameter(Constants.OAUTH.STATE_KEY).equals(getArguments().getString(ARG_STATE_KEY))) {
                        callback.redirectUrlCallbackLoaded(uri);
                    } else {
                        callback.stateKeyNotMatching();
                    }
                    dismiss();
                    return true;
                }
                return false;
            }
        };
    }
}