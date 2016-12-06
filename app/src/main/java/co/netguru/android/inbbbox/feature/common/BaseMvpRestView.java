package co.netguru.android.inbbbox.feature.common;

import com.hannesdorfmann.mosby.mvp.MvpView;

@FunctionalInterface
public interface BaseMvpRestView extends MvpView {

    void showMessageOnServerError(String errorText);
}
