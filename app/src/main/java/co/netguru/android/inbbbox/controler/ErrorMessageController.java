package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ErrorMessageController {

    @Inject
    public ErrorMessageController() {
    }

    public String getError(Throwable throwable) {
        return throwable.getMessage();
    }

}
