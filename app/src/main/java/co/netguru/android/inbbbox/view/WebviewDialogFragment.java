package co.netguru.android.inbbbox.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.webkit.WebViewClient;

public class WebviewDialogFragment extends DialogFragment {

    private WebViewClient client;
    private String url;

    public void setWebViewClient(WebViewClient client) {

        this.client = client;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        FocusableWebView webView = new FocusableWebView(getActivity());

        dialogBuilder.setView(webView);

        webView.loadUrl(url);
        webView.setWebViewClient(client);
        webView.getSettings().setUseWideViewPort(true);

        return dialogBuilder.create();
    }
}
