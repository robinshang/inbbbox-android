package co.netguru.android.inbbbox.controler;

import android.content.res.Resources;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.R;

@Singleton
public class ErrorMessageController {

    private Resources resources;

    @Inject
    public ErrorMessageController(Resources resources) {
        this.resources = resources;
    }

    public String getError(Throwable throwable) {
        return throwable.getMessage();
    }

    public String getMessage(int code) {
        String label;
        if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            label = resources.getString(R.string.authorization_error);

        } else {
            label = resources.getString(R.string.undefined_api_error);
        }

        return label;
    }
}
