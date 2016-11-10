package co.netguru.android.inbbbox.feature.followers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import co.netguru.android.inbbbox.feature.common.BaseMvpFragmentWithWithListTypeSelection;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.di.component.FollowersFragmentComponent;
import co.netguru.android.inbbbox.di.module.FollowersFragmentModule;
import co.netguru.android.inbbbox.feature.followers.adapter.FollowersAdapter;
import co.netguru.android.inbbbox.utils.TextFormatter;
import co.netguru.android.inbbbox.view.LoadMoreScrollListener;

public class FollowersFragment extends BaseMvpFragmentWithWithListTypeSelection<FollowersContract.View, FollowersContract.Presenter>
        implements FollowersContract.View {

    public static final int GRID_VIEW_COLUMN_COUNT = 2;
    private static final int FOLLOWERS_TO_LOAD_MORE = 5;

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
                .plus(new FollowersFragmentModule());
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
        initRecyclerView();
        initEmptyView();
        getPresenter().getFollowedUsersFromServer();
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        adapter.setGridMode(isGridMode);
        recyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
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
    public void showMoreFollowedUsers(List<Follower> followerList) {
        adapter.addMoreFollowers(followerList);
    }

    @Override
    public void hideEmptyLikesInfo() {
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyLikesInfo() {
        emptyView.setVisibility(View.VISIBLE);
    }

    private void initEmptyView() {
        emptyTextDrawable.setBounds(0, 0, emptyViewText.getLineHeight(), emptyViewText.getLineHeight());
        emptyViewText.setText(TextFormatter
                .addDrawableToTextAtFirstSpace(emptyString, emptyTextDrawable), TextView.BufferType.SPANNABLE);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new LoadMoreScrollListener(FOLLOWERS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                presenter.getMoreFollowedUsersFromServer();
            }
        });
    }
}
