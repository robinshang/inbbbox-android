package co.netguru.android.inbbbox.feature.team;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.feature.follower.detail.FollowerDetailsActivity;
import co.netguru.android.inbbbox.feature.follower.detail.FollowerDetailsFragment;
import co.netguru.android.inbbbox.feature.follower.detail.UnFollowUserDialogFragment;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpLceFragmentWithListTypeSelection;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import co.netguru.android.inbbbox.feature.team.adapter.TeamDetailsAdapter;

public class TeamDetailsFragment extends BaseMvpLceFragmentWithListTypeSelection<SwipeRefreshLayout, List<UserWithShots>,
        TeamDetailsContract.View, TeamDetailsContract.Presenter>
        implements TeamDetailsContract.View, UnFollowUserDialogFragment.OnUnFollowClickedListener {

    public static final String TAG = FollowerDetailsFragment.class.getSimpleName();
    private static final String KEY_TEAM = "key_team";
    private static final int GRID_VIEW_COLUMN_COUNT = 2;
    private static final int USERS_TO_LOAD_MORE = 5;
    private static final int RECYCLER_VIEW_HEADER_POSITION = 0;
    private static final int RECYCLER_VIEW_ITEM_SPAN_SIZE = 1;
    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.loadingView)
    ProgressBar progressBar;
    @BindView(R.id.fragment_followers_recycler_view)
    RecyclerView recyclerView;

    @BindColor(R.color.accent)
    int accentColor;

    private MenuItem followMenuItem;
    private MenuItem unFollowMenuItem;
    private TeamDetailsAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    public static TeamDetailsFragment newInstance(UserWithShots team) {
        final Bundle args = new Bundle();
        args.putParcelable(KEY_TEAM, team);

        final TeamDetailsFragment fragment = new TeamDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_followers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initRefreshLayout();
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        adapter.setGridMode(isGridMode);
        recyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
    }

    @Override
    public LceViewState<List<UserWithShots>, TeamDetailsContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<UserWithShots> getData() {
        return adapter.getData();
    }

    @Override
    public void setData(List<UserWithShots> data) {
        adapter.setTeam(getArguments().getParcelable(KEY_TEAM));
        adapter.setTeamMembers(data);
    }

    @Override
    public TeamDetailsContract.Presenter createPresenter() {
        return App.getUserComponent(getContext()).plusTeamDetailsFragmentComponent().getPresenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadTeamData(getArguments().getParcelable(KEY_TEAM));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        followMenuItem = menu.findItem(R.id.action_follow);
        unFollowMenuItem = menu.findItem(R.id.action_unfollow);
        getPresenter().checkIfTeamIsFollowed(getArguments().getParcelable(KEY_TEAM));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_unfollow:
                getPresenter().onUnfollowClick();
                return true;
            case R.id.action_follow:
                getPresenter().onFollowClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void hideProgressBars() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMoreUsers(List<UserWithShots> users) {
        adapter.addMoreMembers(users);
    }

    @Override
    public void showMessageOnServerError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setFollowingMenuIcon(boolean isFollowing) {
        unFollowMenuItem.setVisible(isFollowing);
        followMenuItem.setVisible(!isFollowing);
    }

    @Override
    public void showUnfollowDialog(String name) {
        UnFollowUserDialogFragment
                .newInstance(this, name)
                .show(getFragmentManager(), UnFollowUserDialogFragment.TAG);
    }

    @Override
    public void onUnFollowClicked() {
        getPresenter().unfollowUser();
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(accentColor);
        swipeRefreshLayout.setOnRefreshListener(
                () -> getPresenter().loadTeamData(getArguments().getParcelable(KEY_TEAM)));
    }

    private void initRecyclerView() {
        adapter = new TeamDetailsAdapter(this::showUserDetails);
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
        recyclerView.addOnScrollListener(new LoadMoreScrollListener(USERS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                getPresenter().getMoreMembers(adapter.getTeam());
            }
        });
    }

    private void showUserDetails(UserWithShots userWithShots) {
        FollowerDetailsActivity.startActivity(getContext(), userWithShots);
    }
}
