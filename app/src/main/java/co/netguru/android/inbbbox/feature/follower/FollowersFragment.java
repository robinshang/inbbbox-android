package co.netguru.android.inbbbox.feature.follower;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.utils.TextFormatterUtil;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.feature.follower.adapter.BaseFollowersViewHolder;
import co.netguru.android.inbbbox.feature.follower.adapter.FollowersAdapter;
import co.netguru.android.inbbbox.feature.follower.detail.FollowerDetailsActivity;
import co.netguru.android.inbbbox.feature.main.adapter.RefreshableFragment;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpLceFragmentWithListTypeSelection;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;

public class FollowersFragment extends BaseMvpLceFragmentWithListTypeSelection<SwipeRefreshLayout, List<UserWithShots>, FollowersContract.View, FollowersContract.Presenter>
        implements RefreshableFragment, FollowersContract.View, BaseFollowersViewHolder.OnFollowerClickListener {

    private static final int GRID_VIEW_COLUMN_COUNT = 2;
    private static final int FOLLOWERS_TO_LOAD_MORE = 8;

    @BindDrawable(R.drawable.ic_following_emptystate)
    Drawable emptyTextDrawable;

    @BindString(R.string.fragment_followers_empty_text)
    String emptyString;

    @BindColor(R.color.accent)
    int accentColor;

    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_followers_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_followers_empty_view)
    ScrollView emptyView;
    @BindView(R.id.fragment_followers_empty_text)
    TextView emptyViewText;
    @BindView(R.id.loadingView)
    ProgressBar progressBar;

    private Snackbar loadingMoreSnackbar;
    private FollowersAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    public static FollowersFragment newInstance() {
        return new FollowersFragment();
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
        initRefreshLayout();
        initRecyclerView();
        initEmptyView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loadingMoreSnackbar = null;
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        adapter.setGridMode(isGridMode);
        recyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
    }

    @NonNull
    @Override
    public FollowersContract.Presenter createPresenter() {
        return App.getUserComponent(getContext()).plusFollowersFragmentComponent().getPresenter();
    }

    @NonNull
    @Override
    public LceViewState<List<UserWithShots>, FollowersContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<UserWithShots> getData() {
        return adapter.getData();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getFollowedUsersFromServer();
    }

    @Override
    public void setData(List<UserWithShots> data) {
        adapter.setFollowersList(data);
    }

    @Override
    public void showContent() {
        super.showContent();
        getPresenter().checkDataEmpty(getData());
    }

    @Override
    public void showMoreFollowedUsers(List<UserWithShots> followerList) {
        adapter.addMoreFollowers(followerList);
    }

    @Override
    public void hideLoadingMoreBucketsView() {
        if (loadingMoreSnackbar != null) {
            loadingMoreSnackbar.dismiss();
        }
    }

    @Override
    public void hideEmptyLikesInfo() {
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyLikesInfo() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingMoreFollowersView() {
        if (loadingMoreSnackbar == null && getView() != null) {
            loadingMoreSnackbar = Snackbar.make(getView(), R.string.loading_more_followers, Snackbar.LENGTH_INDEFINITE);
        }
        loadingMoreSnackbar.show();
    }

    @Override
    public void hideProgressBars() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refreshFragmentData() {
        getPresenter().getFollowedUsersFromServer();
    }

    private void initEmptyView() {
        int lineHeight = emptyViewText.getLineHeight();
        //noinspection SuspiciousNameCombination
        emptyTextDrawable.setBounds(0, 0, lineHeight, lineHeight);
        emptyViewText.setText(TextFormatterUtil
                .addDrawableToTextAtFirstSpace(emptyString, emptyTextDrawable), TextView.BufferType.SPANNABLE);
    }

    private void initRecyclerView() {
        adapter = new FollowersAdapter(this);
        gridLayoutManager = new GridLayoutManager(getContext(), GRID_VIEW_COLUMN_COUNT);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new LoadMoreScrollListener(FOLLOWERS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                presenter.getMoreFollowedUsersFromServer();
            }
        });
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(accentColor);
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::getFollowedUsersFromServer);
    }


    @Override
    public void onClick(UserWithShots follower) {
        FollowerDetailsActivity.startActivity(getContext(), follower);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        Toast.makeText(getActivity(), errorText, Toast.LENGTH_LONG).show();
    }
}
