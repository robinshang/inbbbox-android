package co.netguru.android.inbbbox.feature.common;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface BaseMvpRestPresenter<V extends MvpView> extends MvpPresenter<V> {

    void handleHttpErrorResponse(Throwable throwable, String errorText);
}
