package co.netguru.android.inbbbox.feature.likes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.application.App;
import co.netguru.android.inbbbox.di.module.LikesFragmentModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragment;

public class LikesFragment extends BaseMvpFragment<LikesViewContract.View, LikesViewContract.Presenter>
        implements LikesViewContract.View {

    @Inject
    LikesPresenter presenter;

    public static LikesFragment newInstance() {
        return new LikesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.getAppComponent(getContext())
                .plus(new LikesFragmentModule())
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_likes, container, false);
    }

    @NonNull
    @Override
    public LikesViewContract.Presenter createPresenter() {
        return presenter;
    }
}
