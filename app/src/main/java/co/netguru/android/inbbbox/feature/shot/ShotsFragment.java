package co.netguru.android.inbbbox.feature.shot;

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
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.main.adapter.RefreshableFragment;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpViewStateFragment;
import co.netguru.android.inbbbox.feature.shared.view.AutoItemScrollRecyclerView;
import co.netguru.android.inbbbox.feature.shared.view.BallInterpolator;
import co.netguru.android.inbbbox.feature.shared.view.FogFloatingActionMenu;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import co.netguru.android.inbbbox.feature.shot.addtobucket.AddToBucketDialogFragment;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsType;
import co.netguru.android.inbbbox.feature.shot.recycler.ShotSwipeListener;
import co.netguru.android.inbbbox.feature.shot.recycler.ShotsAdapter;
import co.netguru.android.inbbbox.feature.shot.removefrombucket.RemoveFromBucketDialogFragment;

public class ShotsFragment extends BaseMvpViewStateFragment<SwipeRefreshLayout, List<Shot>,
        ShotsContract.View, ShotsContract.Presenter> implements RefreshableFragment, ShotsContract.View, ShotSwipeListener,
        AddToBucketDialogFragment.BucketSelectListener, RemoveFromBucketDialogFragment.BucketSelectListener,
        ViewTreeObserver.OnWindowFocusChangeListener {

    private static final int SHOTS_TO_LOAD_MORE = 5;

    @BindView(R.id.shots_recycler_view)
    AutoItemScrollRecyclerView shotsRecyclerView;

    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fab_menu)
    FogFloatingActionMenu fabMenu;

    @BindView(R.id.container_fog_view)
    View fogContainerView;

    @BindView(R.id.loading_ball_container)
    View loadingBallContainer;

    @BindView(R.id.loading_ball)
    ImageView ballImageView;

    @BindView(R.id.loading_ball_shadow)
    ImageView ballShadowImageView;

    private Animation shadowAnimation;
    private AnimationSet ballAnimation;

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
        initLoadingAnimation();
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
        getPresenter().getShotsFromServer(pullToRefresh);
    }

    @Override
    public void refreshFragmentData() {
        getPresenter().getShotsFromServer(false);
    }

    private void initFabMenu() {
        fabMenu.addFogView(fogContainerView);
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.accent));
        swipeRefreshLayout.setOnRefreshListener(() -> getPresenter().getShotsFromServer(true));
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
    public void showShotRemoveFromBucketSuccess() {
        showTextOnSnackbar(R.string.shots_fragment_remove_shot_from_bucket_success);
    }

    @Override
    public void showDetailsScreenInCommentMode(Shot selectedShot) {
        ShotDetailsRequest request = ShotDetailsRequest.builder()
                .detailsType(ShotDetailsType.DEFAULT)
                .isCommentModeEnabled(true)
                .build();

        shotActionListener.showShotDetails(selectedShot, adapter.getItems(), request);
    }

    @Override
    public void showShotDetails(Shot shot) {
        ShotDetailsRequest request = ShotDetailsRequest.builder()
                .detailsType(ShotDetailsType.DEFAULT)
                .build();

        shotActionListener.showShotDetails(shot, adapter.getItems(), request);
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
    public void showLoadingIndicator(boolean swipeToRefresh) {
        if (swipeToRefresh) {
            showLoadingIndicatorInternal();
        } else {
            ballImageView.getViewTreeObserver().addOnWindowFocusChangeListener(this);
        }
    }

    @Override
    public void hideLoadingIndicator() {
        ballImageView.getViewTreeObserver().removeOnWindowFocusChangeListener(this);
        swipeRefreshLayout.setRefreshing(false);
        loadingBallContainer.post(() -> loadingBallContainer.setVisibility(View.GONE));
        shotsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        Snackbar.make(shotsRecyclerView, errorText, Snackbar.LENGTH_LONG).show();
    }

    private void initLoadingAnimation() {
        Animation scaleAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.ball_animation_scale);
        Animation translateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.ball_animation_translate);
        shadowAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.ball_shadow_animation);

        scaleAnimation.setInterpolator(new BallInterpolator());
        shadowAnimation.setInterpolator(new BallInterpolator());

        ballAnimation = new AnimationSet(false);
        ballAnimation.addAnimation(scaleAnimation);
        ballAnimation.addAnimation(translateAnimation);
    }

    private void showLoadingIndicatorInternal() {
        shotsRecyclerView.setVisibility(View.INVISIBLE);
        loadingBallContainer.setVisibility(View.VISIBLE);
        ballImageView.post(() -> ballImageView.startAnimation(ballAnimation));
        ballShadowImageView.post(() -> ballShadowImageView.startAnimation(shadowAnimation));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ballImageView.getViewTreeObserver().removeOnWindowFocusChangeListener(this);
        showLoadingIndicatorInternal();
    }

    @Override
    public void onBucketToRemoveFromForShotSelect(List<Bucket> list, Shot shot) {
        getPresenter().removeShotFromBuckets(list, shot);
    }

    public interface ShotActionListener {
        void shotLikeStatusChanged();

        void showShotDetails(Shot shot, List<Shot> nearbyShots, ShotDetailsRequest detailsRequest);
    }
}
