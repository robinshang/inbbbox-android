package co.netguru.android.inbbbox.feature.login.oauthwebview;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.app.usercomponent.UserModeType;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import co.netguru.android.inbbbox.feature.main.MainActivity;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpDialogFragment;

public class OauthWebViewDialogFragment
        extends BaseMvpDialogFragment<OauthWebViewDialogFragmentContract.View, OauthWebViewDialogFragmentContract.Presenter>
        implements OauthWebViewDialogFragmentContract.View {

    public static final String TAG = OauthWebViewDialogFragment.class.getSimpleName();

    private static final String ARG_URL = "arg_url";
    private static final String ARG_STATE_KEY = "arg_state_key";

    @BindView(R.id.web_view)
    WebView webView;

    public static OauthWebViewDialogFragment newInstance(String url, String stateKey) {
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_STATE_KEY, stateKey);
        OauthWebViewDialogFragment dialogFragment = new OauthWebViewDialogFragment();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BigDialog);
        dialogFragment.setArguments(args);
        return dialogFragment;
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
        getPresenter().handleKnownOauthError(oauthErrorMessage);
    }

    @Override
    public void finishWithCodeReturn(String receivedCode) {
        getPresenter().handleOauthCodeReceived(receivedCode);
    }

    @Override
    public void finishWithStateKeyNotMatchingError() {
        getPresenter().handleKeysNotMatching();
    }

    @Override
    public void finishWithUnknownError() {
        getPresenter().handleUnknownOauthError();
    }

    @Override
    public void initializeUserMode(UserModeType mode) {
        App.initUserComponent(getActivity(), mode);
    }

    @Override
    public void showNextScreen() {
        if (getActivity() != null) {
            MainActivity.startActivity(getActivity());
            dismiss();
        }
    }

    @Override
    public void showInvalidOauthUrlError() {
        showMessageOnSnackBarDialog(getResources().getString(R.string.invalid_outh_url));
    }

    @Override
    public void showWrongKeyError() {
        showMessageOnSnackBarDialog(getResources().getString(R.string.wrong_oauth_state_key));
    }

    @Override
    public void finish() {
        dismiss();
    }

    @Override
    public void dismiss() {
        if (getActivity() != null) {
            ((LoginActivity) getActivity()).enableLoginButton();
        }
        super.dismiss();
    }

    private void cleanUpWebView() {
        webView.destroy();
        webView = null;
    }

    @Override
    public void onDestroyView() {
        cleanUpWebView();
        getPresenter().detachView(false);
        super.onDestroyView();
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

    @Override
    public void showMessageOnServerError(String errorText) {
        showMessageOnSnackBarDialog(errorText);
    }

    private void showMessageOnSnackBarDialog(String message) {
        Snackbar.make(webView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dismiss();
    }
}