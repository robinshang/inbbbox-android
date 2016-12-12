package co.netguru.android.inbbbox.feature.common;

public interface HttpErrorPresenter {

    void handleHttpErrorResponse(Throwable throwable, String errorText);
}
