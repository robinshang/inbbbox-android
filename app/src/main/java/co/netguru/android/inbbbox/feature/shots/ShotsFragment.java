package co.netguru.android.inbbbox.feature.shots;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.feature.common.BaseMvpViewStateFragment;
import co.netguru.android.inbbbox.feature.main.adapter.RefreshableFragment;
import co.netguru.android.inbbbox.feature.shots.addtobucket.AddToBucketDialogFragment;
import co.netguru.android.inbbbox.feature.shots.recycler.ShotSwipeListener;
import co.netguru.android.inbbbox.feature.shots.recycler.ShotsAdapter;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.view.AutoItemScrollRecyclerView;
import co.netguru.android.inbbbox.view.FogFloatingActionMenu;
import co.netguru.android.inbbbox.view.LoadMoreScrollListener;

public class ShotsFragment extends BaseMvpViewStateFragment<SwipeRefreshLayout, List<Shot>,
        ShotsContract.View, ShotsContract.Presenter> implements RefreshableFragment, ShotsContract.View, ShotSwipeListener,
        AddToBucketDialogFragment.BucketSelectListener {

    private static final int SHOTS_TO_LOAD_MORE = 5;

    @BindView(R.id.shots_recycler_view)
    AutoItemScrollRecyclerView shotsRecyclerView;

    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fab_menu)
    FogFloatingActionMenu fabMenu;

    @BindView(R.id.container_fog_view)
    View fogContainerView;

    private ShotsAdapter adapter;
    private ShotActionListener shotActionListener;

    public static ShotsFragment newInstance() {
        return new ShotsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            shotActionListener = (ShotActionListener) context;
        } catch (ClassCastException e) {
            throw new InterfaceNotImplementedException(e, context.toString(), ShotActionListener.class.getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shots, container, false);
    }

    @NonNull
    @Override
    public ShotsContract.Presenter createPresenter() {
        return App.getUserComponent(getContext()).getShotsComponent().getPresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();
        initRefreshLayout();
        initFabMenu();
    }

    @NonNull
    @Override
    public LceViewState<List<Shot>, ShotsContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @OnClick(R.id.fab_like_menu)
    void onLikeFabClick() {
        int currentItemPosition = shotsRecyclerView.getCurrentItem();
        if (currentItemPosition != RecyclerView.NO_POSITION) {
            getPresenter().likeShot(adapter.getShotFromPosition(currentItemPosition));
        }
    }

    @OnClick(R.id.fab_bucket_menu)
    void onBucketClick() {
        int currentItemPosition = shotsRecyclerView.getCurrentItem();
        if (currentItemPosition != RecyclerView.NO_POSITION) {
            getPresenter().handleAddShotToBucket(
                    adapter.getShotFromPosition(currentItemPosition));
        }
    }

    @OnClick(R.id.fab_comment_menu)
    void onCommentClick() {
        int currentItemPosition = shotsRecyclerView.getCurrentItem();
        if (currentItemPosition != RecyclerView.NO_POSITION) {
            getPresenter()
                    .showCommentInput(adapter.getShotFromPosition(currentItemPosition));
        }
    }

    @OnClick(R.id.fab_follow_menu)
    void onFollowClick() {
        // TODO: 07.11.2016 replace this when feature will be implemented
        Toast.makeText(getContext(), "Follow", Toast.LENGTH_SHORT).show();
    }

    @Override
    public List<Shot> getData() {
        return adapter.getData();
    }

    @Override
    public void setData(List<Shot> data) {
        adapter.setItems(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getShotsFromServer();
    }

    @Override
    public void refreshFragmentData() {
        getPresenter().getShotsFromServer();
    }

    private void initFabMenu() {
        fabMenu.addFogView(fogContainerView);
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.accent));
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::getShotsFromServer);
    }

    private void initRecycler() {
        adapter = new ShotsAdapter(this);
        shotsRecyclerView.setAdapter(adapter);
        shotsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shotsRecyclerView.setHasFixedSize(true);
        shotsRecyclerView.addOnScrollListener(new LoadMoreScrollListener(SHOTS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                getPresenter().getMoreShotsFromServer();
            }
        });
    }

    @Override
    public void showMoreItems(List<Shot> items) {
        adapter.addMoreItems(items);
    }

    @Override
    public void changeShotLikeStatus(Shot shot) {
        adapter.changeShotLikeStatus(shot);
        shotActionListener.shotLikeStatusChanged();
    }

    @Override
    public void closeFabMenu() {
        if (fabMenu.isOpened()) {
            fabMenu.close(true);
        }
    }

    @Override
    public void showBucketChoosing(Shot shot) {
        AddToBucketDialogFragment.newInstance(this, shot)
                .show(getActivity().getSupportFragmentManager(), AddToBucketDialogFragment.TAG);
    }

    @Override
    public void showBucketAddSuccess() {
        showTextOnSnackbar(R.string.shots_fragment_add_shot_to_bucket_success);
    }

    @Override
    public void showDetailsScreenInCommentMode(Shot selectedShot) {
        shotActionListener.showShotDetails(selectedShot, true);
    }

    @Override
    public void showShotDetails(Shot shot) {
        shotActionListener.showShotDetails(shot, false);
    }

    @Override
    public void onShotLikeSwipe(Shot shot) {
        getPresenter().likeShot(shot);
    }

    @Override
    public void onAddShotToBucketSwipe(Shot shot) {
        getPresenter().handleAddShotToBucket(shot);
    }

    @Override
    public void onCommentShotSwipe(Shot shot) {
        getPresenter().showCommentInput(shot);
    }

    @Override
    public void onBucketForShotSelect(Bucket bucket, Shot shot) {
        getPresenter().addShotToBucket(bucket, shot);
    }

    @Override
    public void onShotSelected(Shot shot) {
        getPresenter().showShotDetails(shot);
    }

    @Override
    public void showLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        Snackbar.make(shotsRecyclerView, errorText, Snackbar.LENGTH_LONG).show();
    }

    public interface ShotActionListener {
        void shotLikeStatusChanged();

        void showShotDetails(Shot shot, boolean inCommentMode);
    }
}
