package co.netguru.android.inbbbox.feature.shared.base;

public interface ErrorPresenter {

    void handleError(Throwable throwable, String errorText);
}
