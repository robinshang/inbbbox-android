package co.netguru.android.inbbbox.controller;

import android.content.res.Resources;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import retrofit2.adapter.rxjava.HttpException;

public class ErrorController {

    private final Resources resources;

    private static final int HTTP_TOO_MANY_REQUESTS_429 = 429;
    private static final int HTTP_FORBIDDEN_403 = 403;
    private static final int HTTP_UNAUTHORIZED_401 = 401;

    @Inject
    public ErrorController(Resources resources) {
        this.resources = resources;
    }

    public String getThrowableMessage(Throwable throwable) {
        String message;

        if (throwable instanceof HttpException) {
            message = getMessageBasedOnErrorCode(((HttpException) throwable).code());

        } else {
            message = throwable.getMessage();
        }

        return message;
    }

    public String getMessageBasedOnErrorCode(int code) {
        String message;

        if (code == HTTP_TOO_MANY_REQUESTS_429) {
            message = resources.getString(R.string.api_error_429);

        } else if (code == HTTP_FORBIDDEN_403) {
            message = resources.getString(R.string.error_to_low_rank);

        } else if (code == HTTP_UNAUTHORIZED_401) {
            message = resources.getString(R.string.authorization_error);

        } else {
            message = resources.getString(R.string.undefined_api_error);
        }

        return message;
    }
}
