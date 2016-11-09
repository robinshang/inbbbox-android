package co.netguru.android.inbbbox.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import co.netguru.android.inbbbox.R;
import timber.log.Timber;

public class WebviewDialogFragment extends DialogFragment {

    public static final String TAG = WebviewDialogFragment.class.getSimpleName();
    private static final String ARG_URL = "arg_url";

    private OnRedirectUrlCallbackListener callback;

    private final WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            boolean result = true;
            if (url.contains(getString(R.string.redirectUriScheme))) {
                Timber.d(url);
                callback.redirectUrlCallbackLoaded(url);
                result = false;
            } else {
                view.loadUrl(url);
            }
            return result;
        }
    };

    public static WebviewDialogFragment newInstance(String url) {
        WebviewDialogFragment dialogFragment = new WebviewDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        FocusableWebView webView = new FocusableWebView(getActivity());

        dialogBuilder.setView(webView);

        webView.loadUrl(getArguments().getString(ARG_URL));
        webView.setWebViewClient(webViewClient);
        webView.getSettings().setUseWideViewPort(true);

        return dialogBuilder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnRedirectUrlCallbackListener) context;
        } catch (ClassCastException e) {
            Timber.e(e.getMessage());
            throw new ClassCastException(context.toString()
                    + " must implement OnRedirectUrlCallbackListener");
        }
    }
}
