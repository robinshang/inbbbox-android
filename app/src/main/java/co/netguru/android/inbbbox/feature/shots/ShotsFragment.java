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

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.ShotsComponent;
import co.netguru.android.inbbbox.di.module.ShotsModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpViewStateFragment;
import co.netguru.android.inbbbox.feature.shots.addtobucket.AddToBucketDialogFragment;
import co.netguru.android.inbbbox.feature.shots.recycler.ShotSwipeListener;
import co.netguru.android.inbbbox.feature.shots.recycler.ShotsAdapter;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.view.AutoItemScrollRecyclerView;
import co.netguru.android.inbbbox.view.FogFloatingActionMenu;
import co.netguru.android.inbbbox.view.LoadMoreScrollListener;

public class ShotsFragment
        extends BaseMvpViewStateFragment<ShotsContract.View, ShotsContract.Presenter>
        implements ShotsContract.View, ShotSwipeListener,
        AddToBucketDialogFragment.BucketSelectListener {

    private static final int SHOTS_TO_LOAD_MORE = 5;

    @Inject
    ShotsAdapter adapter;

    @BindView(R.id.shots_recycler_view)
    AutoItemScrollRecyclerView shotsRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fab_menu)
    FogFloatingActionMenu fabMenu;

    @BindView(R.id.container_fog_view)
    View fogContainerView;

    private ShotsComponent component;
    private ShotActionListener shotActionListener;

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

    public static ShotsFragment newInstance() {
        return new ShotsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            shotActionListener = (ShotActionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ShotActionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponent();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shots, container, false);
    }

    private void initComponent() {
        component = App.getAppComponent(getContext())
                .plus(new ShotsModule(this));
        component.inject(this);
    }

    @NonNull
    @Override
    public ShotsContract.Presenter createPresenter() {
        return component.getPresenter();
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
    public ViewState createViewState() {
        return new ShotsViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        getPresenter().getShotsFromServer();
    }

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
    public void showItems(List<Shot> items) {
        adapter.setItems(items);
        ((ShotsViewState) viewState).setDataList(items);
    }

    @Override
    public void showMoreItems(List<Shot> items) {
        adapter.addMoreItems(items);
        ((ShotsViewState) viewState).addMoreData(items);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(shotsRecyclerView, error, Snackbar.LENGTH_LONG).show();
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

    public interface ShotActionListener {
        void shotLikeStatusChanged();

        void showShotDetails(Shot shot, boolean inCommentMode);


    }

    @Override
    public void showLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
