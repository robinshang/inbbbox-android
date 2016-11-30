package co.netguru.android.inbbbox.feature.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseMvpViewStateFragment<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpViewStateFragment<V, P> {

    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected void showTextOnSnackbar(@StringRes int stringRes) {
        if (getView() != null) {
            Snackbar.make(getView(), stringRes, Snackbar.LENGTH_LONG).show();
        }
    }
}
