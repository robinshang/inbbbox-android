package co.netguru.android.inbbbox.feature.common;

public interface ErrorPresenter {

    void handleError(Throwable throwable, String errorText);
}
