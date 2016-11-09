package co.netguru.android.inbbbox.feature.followers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;

import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.di.component.FollowersFragmentComponent;
import co.netguru.android.inbbbox.di.module.FollowersFragmentModule;
import co.netguru.android.inbbbox.feature.common.BaseFragmentWithMenu;
import co.netguru.android.inbbbox.feature.followers.adapter.FollowersAdapter;

public class FollowersFragment extends BaseFragmentWithMenu<FollowersContract.View, FollowersContract.Presenter>
        implements FollowersContract.View {

    @BindDrawable(R.drawable.ic_following_emptystate)
    Drawable emptyTextDrawable;

    @BindString(R.string.fragment_followers_empty_text)
    String emptyString;

    @BindView(R.id.fragment_followers_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_followers_empty_view)
    ScrollView emptyView;
    @BindView(R.id.fragment_followers_empty_text)
    TextView emptyViewText;

    @Inject
    FollowersAdapter adapter;
    @Inject
    GridLayoutManager gridLayoutManager;
    @Inject
    LinearLayoutManager linearLayoutManager;

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

    @Override
    protected void changeGridMode(boolean isGridMode) {
        adapter.setGridMode(isGridMode);
        recyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
        recyclerView.setAdapter(adapter);
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
        emptyTextDrawable.setBounds(0, 0, emptyViewText.getLineHeight(), emptyViewText.getLineHeight());
        getPresenter().getFollowedUsersFromServer();
        getPresenter().addIconToText(emptyString, emptyTextDrawable);
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

    @Override
    public void hideEmptyLikesInfo() {
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyLikesInfo() {
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setEmptyViewText(SpannableStringBuilder spannableStringBuilder) {
        emptyViewText.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
    }
}
