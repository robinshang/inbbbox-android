package co.netguru.android.inbbbox.controler;

import retrofit2.adapter.rxjava.HttpException;

public class ErrorController {

    private static final int ERROR_429 = 429;

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

        if (httpException.code() == ERROR_429) {
            message = "You're doing too much work! Hold on";
        } else {
            message = httpException.message();
        }

        return message;
    }
}
