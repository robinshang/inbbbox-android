package co.netguru.android.inbbbox.controler;

import android.content.res.Resources;

import javax.inject.Inject;

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
           message = ((HttpException) throwable).message();

        } else {
            message = resources.getString(R.string.undefined_api_error);
        }

        return message;
    }
}
