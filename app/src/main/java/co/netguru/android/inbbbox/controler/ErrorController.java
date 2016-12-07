package co.netguru.android.inbbbox.controler;

import android.content.res.Resources;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.R;
import retrofit2.adapter.rxjava.HttpException;

public class ErrorController {

    private final Resources resources;

    public ErrorController(Resources resources) {
        this.resources = resources;
    }

    public String getThrowableMessage(Throwable throwable) {
        String message;

        if(throwable instanceof HttpException){
            message = getMessageBasedOnErrorCode((HttpException) throwable);

        } else {
            message = throwable.getMessage();
        }

        return message;
    }

    private String getMessageBasedOnErrorCode(HttpException httpException) {
        String message;

        if (httpException.code() == Constants.CODES.API_ERROR_429) {
            message = resources.getString(R.string.api_error_429);
        } else {
            message = httpException.message();
        }

        return message;
    }
}
