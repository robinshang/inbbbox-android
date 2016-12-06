package co.netguru.android.inbbbox.feature.followers.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.FollowerDetailsFragmentComponent;
import co.netguru.android.inbbbox.di.module.FollowerDetailsFragmentModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpLceFragmentWithListTypeSelection;
import co.netguru.android.inbbbox.feature.followers.details.adapter.FollowerDetailsAdapter;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.view.LoadMoreScrollListener;

import static butterknife.ButterKnife.findById;

public class FollowerDetailsFragment extends BaseMvpLceFragmentWithListTypeSelection<SwipeRefreshLayout, List<Shot>,
        FollowerDetailsContract.View, FollowerDetailsContract.Presenter> implements FollowerDetailsContract.View {

    private static final int GRID_VIEW_COLUMN_COUNT = 2;
    public static final String TAG = FollowerDetailsFragment.class.getSimpleName();
    private static final String FOLLOWER_KEY = "follower_key";
    private static final int SHOTS_TO_LOAD_MORE = 10;
    private static final int RECYCLER_VIEW_HEADER_POSITION = 0;
    private static final int RECYCLER_VIEW_ITEM_SPAN_SIZE = 1;

    @BindString(R.string.fragment_follower_details_dialog_text)
    String dialogText;

    @BindColor(R.color.accent)
    int accentColor;

    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_follower_details_recycler_view)
    RecyclerView recyclerView;

    @Inject
    FollowerDetailsAdapter adapter;

    private OnFollowedShotActionListener onUnFollowCompletedListener;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private FollowerDetailsFragmentComponent component;

    public static FollowerDetailsFragment newInstance(Follower follower) {
        final Bundle args = new Bundle();
        args.putParcelable(FOLLOWER_KEY, follower);

        final FollowerDetailsFragment fragment = new FollowerDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onUnFollowCompletedListener = (OnFollowedShotActionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFollowedShotActionListener");
        }
        component = App.getAppComponent(getContext())
                .plus(new FollowerDetailsFragmentModule(shot -> getPresenter().showShotDetails(shot)));
        component.inject(this);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_follow:
                getPresenter().onUnFollowClick();
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
        return component.getPresenter();
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
        onUnFollowCompletedListener.unFollowCompleted();
    }

    @Override
    public void showError(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void openShotDetailsScreen(Shot shot) {
        onUnFollowCompletedListener.showShotDetails(shot);
    }

    @Override
    public void showUnFollowDialog(String username) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_unfollow_user, null);
        final TextView textView = findById(view, R.id.dialog_unfollow_text);
        textView.setText(String.format(dialogText, username));
        new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                .setView(view)
                .setPositiveButton(R.string.action_unfollow, (dialog, which) -> getPresenter().unFollowUser())
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
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
        void showShotDetails(Shot shot);

        void unFollowCompleted();
    }
}
