package co.netguru.android.inbbbox.common.base;

public interface ErrorPresenter {

    void handleError(Throwable throwable, String errorText);
}
