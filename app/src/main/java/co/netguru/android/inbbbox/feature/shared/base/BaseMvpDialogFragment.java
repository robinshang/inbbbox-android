package co.netguru.android.inbbbox.feature.shared.base;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.delegate.BaseMvpDelegateCallback;
import com.hannesdorfmann.mosby.mvp.delegate.FragmentMvpDelegate;
import com.hannesdorfmann.mosby.mvp.delegate.FragmentMvpDelegateImpl;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A DialogFragment that uses an {@link MvpPresenter} to implement a Model-View-Presenter
 * architecture , which is not a part of Mosby library and use provided delegates instead.
 */
public abstract class BaseMvpDialogFragment<V extends MvpView, P extends MvpPresenter<V>> extends DialogFragment
        implements BaseMvpDelegateCallback<V, P>, MvpView {

    private final FragmentMvpDelegate<V, P> mvpDelegate = new FragmentMvpDelegateImpl<>(this);

    protected P presenter;
    private Unbinder unbinder;

    @Override
    public P getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(@NonNull P presenter) {
        this.presenter = presenter;
    }

    @Override
    public V getMvpView() {
        return (V) this;
    }

    @Override
    public boolean isRetainInstance() {
        return getRetainInstance();
    }

    @Override
    public boolean shouldInstanceBeRetained() {
        Activity activity = getActivity();
        boolean changingConfig = activity != null && activity.isChangingConfigurations();
        return getRetainInstance() && changingConfig;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mvpDelegate.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpDelegate.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpDelegate.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mvpDelegate.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mvpDelegate.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mvpDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mvpDelegate.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvpDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mvpDelegate.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mvpDelegate.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mvpDelegate.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mvpDelegate.onDetach();
    }
}
