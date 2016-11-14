package co.netguru.android.inbbbox.feature.followers.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.FollowerDetailsFragmentComponent;
import co.netguru.android.inbbbox.di.module.FollowerDetailsFragmentModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragmentWithWithListTypeSelection;

public class FollowerDetailsFragment extends BaseMvpFragmentWithWithListTypeSelection<FollowerDetailsContract.View,
        FollowerDetailsContract.Presenter> implements FollowerDetailsContract.View {

    public static final String TAG = "FollowerDetailsFragmentTag";

    private FollowerDetailsFragmentComponent component;

    public static FollowerDetailsFragment newInstance() {

        Bundle args = new Bundle();

        FollowerDetailsFragment fragment = new FollowerDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        component = App.getAppComponent(getContext())
                .plus(new FollowerDetailsFragmentModule());
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follower_details, container, false);
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        // TODO: 14.11.2016 Fill this method
    }

    @NonNull
    @Override
    public FollowerDetailsContract.Presenter createPresenter() {
        return component.getPresenter();
    }
}
