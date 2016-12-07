package co.netguru.android.inbbbox.controler;

import android.content.res.Resources;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.R;
import retrofit2.adapter.rxjava.HttpException;

public class ErrorController {

    private final Resources resources;

    @Inject
    public ErrorController(Resources resources) {
        this.resources = resources;
    }

    public String getThrowableMessage(Throwable throwable) {
        String message;

        if(throwable instanceof HttpException){
            message = getMessageBasedOnErrorCode(((HttpException) throwable).code());

        } else {
            message = throwable.getMessage();
        }

        return message;
    }

    public String getMessageBasedOnErrorCode(int code) {
        String message;

        if (code == Constants.CODES.API_ERROR_429) {
            message = resources.getString(R.string.api_error_429);

        } else if (code == HttpURLConnection.HTTP_FORBIDDEN) {
            message = resources.getString(R.string.error_to_low_rank);

        } else if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            message = resources.getString(R.string.authorization_error);

        } else {
            message = resources.getString(R.string.undefined_api_error);
        }

        return message;
    }
}
