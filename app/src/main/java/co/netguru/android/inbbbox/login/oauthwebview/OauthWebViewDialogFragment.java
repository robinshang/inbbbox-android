package co.netguru.android.inbbbox.login.oauthwebview;

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
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.common.base.BaseMvpDialogFragment;
import timber.log.Timber;

public class OauthWebViewDialogFragment
        extends BaseMvpDialogFragment<OauthWebViewDialogFragmentContract.View, OauthWebViewDialogFragmentContract.Presenter>
        implements OauthWebViewDialogFragmentContract.View {

    public static final String TAG = OauthWebViewDialogFragment.class.getSimpleName();

    private static final String ARG_URL = "arg_url";
    private static final String ARG_STATE_KEY = "arg_state_key";

    @BindView(R.id.web_view)
    WebView webView;

    private OauthWebViewListener callback;

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
            throw new InterfaceNotImplementedException(e, context.toString(), OauthWebViewListener.class.getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_oauth_web_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView.setWebViewClient(getWebViewClient());
        webView.getSettings().setUseWideViewPort(true);

        String url = getArguments().getString(ARG_URL);
        String stateKey = getArguments().getString(ARG_STATE_KEY);
        getPresenter().handleData(url, stateKey);
    }

    @NonNull
    @Override
    public OauthWebViewDialogFragmentContract.Presenter createPresenter() {
        return App.getAppComponent(getContext()).plusOauthWebViewDialogFragmentComponent()
                .getOauthWebViewDialogFragmentPresenter();
    }

    @Override
    public void loadUrl(String url) {
        webView.loadUrl(url);
    }

    @Override
    public void finishWithError(String oauthErrorMessage) {
        callback.onOauthKnownError(oauthErrorMessage);
        dismiss();
    }

    @Override
    public void finishWithCodeReturn(String receivedCode) {
        callback.onOauthCodeReceive(receivedCode);
        dismiss();
    }

    @Override
    public void finishWithStateKeyNotMatchingError() {
        callback.onOauthStateKeyNotMatching();
        dismiss();
    }

    @Override
    public void finishWithUnknownError() {
        callback.onOauthUnknownError();
        dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        callback.onOauthFragmentClose();
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
                return getPresenter().shouldOverrideUrlLoading(uri);
            }
        };
    }
}