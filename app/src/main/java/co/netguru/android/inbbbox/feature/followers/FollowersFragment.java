package co.netguru.android.inbbbox.feature.followers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.application.App;
import co.netguru.android.inbbbox.data.ui.Follower;
import co.netguru.android.inbbbox.di.component.FollowersFragmentComponent;
import co.netguru.android.inbbbox.di.module.FollowersFragmentModule;
import co.netguru.android.inbbbox.feature.common.BaseFragmentWithMenu;
import co.netguru.android.inbbbox.feature.followers.adapter.FollowersAdapter;

public class FollowersFragment extends BaseFragmentWithMenu<FollowersContract.View, FollowersContract.Presenter>
        implements FollowersContract.View {

    @Inject
    FollowersAdapter adapter;

    private FollowersFragmentComponent component;

    public static FollowersFragment newInstance() {
        return new FollowersFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        component = App.getAppComponent(getContext())
                .plus(new FollowersFragmentModule(getContext()));
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_followers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().getFollowedUsersFromServer();
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    public FollowersContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @Override
    public void showFollowedUsers(List<Follower> followerList) {
        adapter.setFollowersList(followerList);
    }
}
