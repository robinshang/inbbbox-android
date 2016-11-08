package co.netguru.android.inbbbox.controler;

import android.content.res.Resources;

import javax.inject.Inject;
import javax.inject.Singleton;

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

}
