package co.netguru.android.inbbbox.feature.follower.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.Follower;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.follower.detail.adapter.FollowerDetailsAdapter;
import co.netguru.android.inbbbox.feature.followers.details.FollowUserDialogFragment;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpLceFragmentWithListTypeSelection;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import timber.log.Timber;

public class FollowerDetailsFragment extends BaseMvpLceFragmentWithListTypeSelection<SwipeRefreshLayout, List<Shot>,
        FollowerDetailsContract.View, FollowerDetailsContract.Presenter>
        implements FollowerDetailsContract.View,
        UnFollowUserDialogFragment.OnUnFollowClickedListener,
        FollowUserDialogFragment.OnFollowClickedListener {

    public static final String TAG = FollowerDetailsFragment.class.getSimpleName();
    private static final int GRID_VIEW_COLUMN_COUNT = 2;
    private static final String FOLLOWER_KEY = "follower_key";
    private static final String USER_KEY = "user_key";
    private static final int SHOTS_TO_LOAD_MORE = 10;
    private static final int RECYCLER_VIEW_HEADER_POSITION = 0;
    private static final int RECYCLER_VIEW_ITEM_SPAN_SIZE = 1;

    private MenuItem followMenuItem;
    private MenuItem unfollowMenuItem;

    @BindColor(R.color.accent)
    int accentColor;

    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_follower_details_recycler_view)
    RecyclerView recyclerView;

    private OnFollowedShotActionListener onChangeFollowingStatusCompletedListener;
    private FollowerDetailsAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    public static FollowerDetailsFragment newInstanceWithFollower(Follower follower) {
        final Bundle args = new Bundle();
        args.putParcelable(FOLLOWER_KEY, follower);

        final FollowerDetailsFragment fragment = new FollowerDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static FollowerDetailsFragment newInstanceWithUser(User user) {
        final Bundle args = new Bundle();
        args.putParcelable(USER_KEY, user);

        final FollowerDetailsFragment fragment = new FollowerDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onChangeFollowingStatusCompletedListener = (OnFollowedShotActionListener) context;
        } catch (ClassCastException e) {
            Timber.e(e, "must implement OnFollowedShotActionListener");
            throw new InterfaceNotImplementedException(e, context.toString(), OnFollowedShotActionListener.class.getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follower_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRefreshLayout();
        initRecyclerView();
        getPresenter().followerDataReceived(getArguments().getParcelable(FOLLOWER_KEY));
        getPresenter().userDataReceived(getArguments().getParcelable(USER_KEY));
    }

    @Override
    public void setFollowingMenuIcon(boolean isFollowed) {
        unfollowMenuItem.setVisible(isFollowed);
        followMenuItem.setVisible(!isFollowed);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        followMenuItem = menu.findItem(R.id.action_follow);
        unfollowMenuItem = menu.findItem(R.id.action_unfollow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_unfollow:
                getPresenter().onUnFollowClick();
                return true;
            case R.id.action_follow:
                getPresenter().onFollowClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        adapter.setGridMode(isGridMode);
        recyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
    }

    @NonNull
    @Override
    public FollowerDetailsContract.Presenter createPresenter() {
        return App.getUserComponent(getContext()).plusFollowersDetailsFragmentComponent().getPresenter();
    }

    @NonNull
    @Override
    public LceViewState<List<Shot>, FollowerDetailsContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Shot> getData() {
        return adapter.getData();
    }

    @Override
    public void setData(List<Shot> data) {
        getFollowerData();
        adapter.setUserShots(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getFollowerData();
    }

    @Override
    public void showFollowerData(Follower follower) {
        adapter.setFollowerAdapterData(follower);

        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(follower.name());
        }
    }

    @Override
    public void showMoreUserShots(List<Shot> shotList) {
        adapter.addMoreUserShots(shotList);
    }

    @Override
    public void showFollowersList() {
        onChangeFollowingStatusCompletedListener.onFollowingStatusChangeCompleted();
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void openShotDetailsScreen(Shot shot, List<Shot> allShots, long userId) {
        onChangeFollowingStatusCompletedListener.showShotDetails(shot, allShots, userId);
    }

    @Override
    public void showUnFollowDialog(String username) {
        UnFollowUserDialogFragment
                .newInstance(this, username)
                .show(getFragmentManager(), UnFollowUserDialogFragment.TAG);
    }

    @Override
    public void showFollowDialog(String username) {
        FollowUserDialogFragment
                .newInstance(this, username)
                .show(getFragmentManager(), FollowUserDialogFragment.TAG);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        Snackbar.make(recyclerView, errorText, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onUnFollowClicked() {
        getPresenter().unFollowUser();
    }

    @Override
    public void onFollowClicked() {
        getPresenter().followUser();
    }

    private void getFollowerData() {
        final Follower follower = getArguments().getParcelable(FOLLOWER_KEY);
        getPresenter().followerDataReceived(follower);
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(accentColor);
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::refreshUserShots);
    }

    private void initRecyclerView() {
        adapter = new FollowerDetailsAdapter(getPresenter()::showShotDetails);
        gridLayoutManager = new GridLayoutManager(getContext(), GRID_VIEW_COLUMN_COUNT);
        linearLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == RECYCLER_VIEW_HEADER_POSITION) {
                    return GRID_VIEW_COLUMN_COUNT;
                }
                return RECYCLER_VIEW_ITEM_SPAN_SIZE;
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new LoadMoreScrollListener(SHOTS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                getPresenter().getMoreUserShotsFromServer();
            }
        });
    }

    public interface OnFollowedShotActionListener {
        void showShotDetails(Shot shot, List<Shot> allShots, long userId);

        void onFollowingStatusChangeCompleted();
    }
}
