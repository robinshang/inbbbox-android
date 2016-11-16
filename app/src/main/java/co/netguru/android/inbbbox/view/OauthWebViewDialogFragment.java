package co.netguru.android.inbbbox.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.R;
import timber.log.Timber;

public class OauthWebViewDialogFragment extends DialogFragment {

    public static final String TAG = OauthWebViewDialogFragment.class.getSimpleName();

    private static final String ARG_URL = "arg_url";
    private static final String ARG_STATE_KEY = "arg_state_key";

    @BindView(R.id.web_view)
    WebView webView;

    private OauthWebViewListener callback;
    private Unbinder unbinder;

    public static OauthWebViewDialogFragment newInstance(String url, String stateKey) {
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_STATE_KEY, stateKey);
        OauthWebViewDialogFragment dialogFragment = new OauthWebViewDialogFragment();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BigDialog);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_oauth_web_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        webView.setWebViewClient(getWebViewClient());
        webView.loadUrl(getArguments().getString(ARG_URL));
        webView.getSettings().setUseWideViewPort(true);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                if (uri.toString().startsWith(BuildConfig.DRIBBBLE_OAUTH_REDIRECT)) {
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