package co.netguru.android.inbbbox.controler;

import android.content.res.Resources;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.R;

@Singleton
public class ErrorMessageController {

    private final Resources resources;

    @Inject
    public ErrorMessageController(Resources resources) {
        this.resources = resources;
    }

    public String getErrorMessageLabel(Throwable throwable) {
        String message;
        if (throwable.getMessage().contains(Integer.toString(HttpURLConnection.HTTP_FORBIDDEN))) {
            message = resources.getString(R.string.error_to_low_rank);
        } else {
            message = throwable.getMessage();
        }
        return message;
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
