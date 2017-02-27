package co.netguru.android.inbbbox.feature.user.shots;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpViewStateFragment;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import co.netguru.android.inbbbox.feature.user.info.team.ShotActionListener;
import co.netguru.android.inbbbox.feature.user.shots.adapter.UserShotsAdapter;
import timber.log.Timber;

public class UserShotsFragment extends BaseMvpViewStateFragment<SwipeRefreshLayout, List<Shot>,
        UserShotsContract.View, UserShotsContract.Presenter>
        implements UserShotsContract.View {

    public static final String TAG = UserShotsFragment.class.getSimpleName();

    private static final int GRID_VIEW_COLUMN_COUNT = 2;
    private static final String USER_KEY = "user_key";
    private static final int SHOTS_TO_LOAD_MORE = 10;

    @BindColor(R.color.accent)
    int accentColor;

    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fragment_follower_details_recycler_view)
    RecyclerView recyclerView;

    private ShotActionListener shotActionListener;
    private UserShotsAdapter adapter;

    public static UserShotsFragment newInstance(User user) {
        final Bundle args = new Bundle();
        args.putParcelable(USER_KEY, user);

        final UserShotsFragment fragment = new UserShotsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            shotActionListener = (ShotActionListener) context;
        } catch (ClassCastException e) {
            Timber.e(e, "must implement OnFollowedShotActionListener");
            throw new InterfaceNotImplementedException(e, context.toString(), ShotActionListener.class.getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_shots, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRefreshLayout();
        initRecyclerView();
    }

    @NonNull
    @Override
    public UserShotsContract.Presenter createPresenter() {
        UserShotsModule module = new UserShotsModule(getArguments().getParcelable(USER_KEY));
        return App.getUserComponent(getContext())
                .plusUserShotsComponent(module).getPresenter();
    }

    @NonNull
    @Override
    public LceViewState<List<Shot>, UserShotsContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Shot> getData() {
        return adapter.getData();
    }

    @Override
    public void setData(List<Shot> data) {
        adapter.setUserShots(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().userDataReceived(getArguments().getParcelable(USER_KEY));
    }

    @Override
    public void showMoreUserShots(List<Shot> shotList) {
        adapter.addMoreUserShots(shotList);
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void openShotDetailsScreen(Shot shot, List<Shot> allShots, long userId) {
        shotActionListener.showShotDetails(shot, allShots, userId);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        Snackbar.make(recyclerView, errorText, Snackbar.LENGTH_SHORT).show();
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(accentColor);
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::refreshUserShots);
    }

    private void initRecyclerView() {
        adapter = new UserShotsAdapter(getPresenter()::showShotDetails);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                GRID_VIEW_COLUMN_COUNT);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(getScrollListener());
    }

    private LoadMoreScrollListener getScrollListener() {
        return new LoadMoreScrollListener(SHOTS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                getPresenter().getMoreUserShotsFromServer();
            }
        };
    }

}
