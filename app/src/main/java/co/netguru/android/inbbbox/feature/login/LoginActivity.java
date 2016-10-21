package co.netguru.android.inbbbox.feature.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import co.netguru.android.commons.di.WithComponent;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.application.App;
import co.netguru.android.inbbbox.di.component.LoginComponent;
import co.netguru.android.inbbbox.di.module.LoginModule;
import co.netguru.android.inbbbox.feature.main.MainActivity;
import co.netguru.android.inbbbox.view.FocusableWebView;
import timber.log.Timber;

public class LoginActivity extends MvpActivity<LoginContract.View, LoginContract.Presenter>
        implements LoginContract.View, WithComponent<LoginComponent> {

    private AlertDialog loginDialog;

    @OnClick(R.id.btn_login)
    void onLoginClick() {
        getPresenter().showLoginView();
    }


    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            boolean result = true;
            if (url.contains(getString(R.string.redirectUriScheme))) {
                Timber.d(url);
                getPresenter().handleOauthLoginResponse(Uri.parse(url));
                result = false;
            } else {
                view.loadUrl(url);
            }
            return result;
        }
    };

    private LoginComponent component;

    public static void startActivity(Context context) {
        final Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initComponent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    private void initComponent() {
        this.component = App.getAppComponent(this)
                .plus(new LoginModule());
        component.inject(this);
    }

    @NonNull
    @Override
    public LoginContract.Presenter createPresenter() {
        return getComponent().getLoginPresenter();
    }

    @Override
    public LoginComponent getComponent() {
        return this.component;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void handleOauthUri(String uriString) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        FocusableWebView webView = new FocusableWebView(this);
        webView.loadUrl(uriString);
        webView.setWebViewClient(webViewClient);
        webView.getSettings().setUseWideViewPort(true);

        dialogBuilder.setView(webView);
        loginDialog = dialogBuilder.create();
        loginDialog.show();
    }

    @Override
    public void showApiError(String oauthErrorMessage) {
        Toast.makeText(this, oauthErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNextScreen() {
        MainActivity.startActivity(this);
        finish();
    }

    @Override
    public void closeLoginDialog() {
        if (loginDialog != null) {
            loginDialog.dismiss();
        }
    }
}
