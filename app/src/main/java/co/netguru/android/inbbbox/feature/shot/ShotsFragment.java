package co.netguru.android.inbbbox.feature.shot;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.analytics.AnalyticsEventLogger;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.main.adapter.RefreshableFragment;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpViewStateFragment;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;
import co.netguru.android.inbbbox.feature.shared.view.AutoItemScrollRecyclerView;
import co.netguru.android.inbbbox.feature.shared.view.BallInterpolator;
import co.netguru.android.inbbbox.feature.shared.view.CustomLinearLayoutManager;
import co.netguru.android.inbbbox.feature.shared.view.FogFloatingActionMenu;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import co.netguru.android.inbbbox.feature.shared.view.OnAnimationEndListener;
import co.netguru.android.inbbbox.feature.shared.view.TwoCoveredShotsAnimationView;
import co.netguru.android.inbbbox.feature.shot.addtobucket.AddToBucketDialogFragment;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsType;
import co.netguru.android.inbbbox.feature.shot.recycler.DetailsVisibilityChangeEmitter;
import co.netguru.android.inbbbox.feature.shot.recycler.DetailsVisibilityChangeListener;
import co.netguru.android.inbbbox.feature.shot.recycler.ShotSwipeListener;
import co.netguru.android.inbbbox.feature.shot.recycler.ShotsAdapter;
import co.netguru.android.inbbbox.feature.shot.removefrombucket.RemoveFromBucketDialogFragment;

public class ShotsFragment extends BaseMvpViewStateFragment<SwipeRefreshLayout, List<Shot>,
        ShotsContract.View, ShotsContract.Presenter> implements RefreshableFragment, ShotsContract.View, ShotSwipeListener,
        AddToBucketDialogFragment.BucketSelectListener, RemoveFromBucketDialogFragment.BucketSelectListener,
        ShotPeekAndPop.OnGeneralActionListener, DetailsVisibilityChangeEmitter {

    private static final String KEY_SHOULD_SHOW_SHOTS_ANIMATION = "key:should_show_shots_animation";
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

    @BindView(R.id.shots_animation_container)
    TwoCoveredShotsAnimationView twoCoveredShotsAnimationView;
    @Inject
    ShotsAdapter adapter;
    @Inject
    AnalyticsEventLogger analyticsEventLogger;
    private CustomLinearLayoutManager customLinearLayoutManager;
    private Animation shadowAnimation;
    private AnimationSet ballAnimation;
    private ShotPeekAndPop peekAndPop;
    private ShotActionListener shotActionListener;
    private DetailsVisibilityChangeListener detailsVisibilityChangeListener;
    private ShotsComponent component;

    public static ShotsFragment newInstance(boolean shouldShowShotsAnimation) {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_SHOULD_SHOW_SHOTS_ANIMATION, shouldShowShotsAnimation);
        final ShotsFragment fragment = new ShotsFragment();
        fragment.setArguments(bundle);

        return fragment;
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
        initPeekAndPop();
        initComponent();
        return inflater.inflate(R.layout.fragment_shots, container, false);
    }

    @Override
    public void onViewStateInstanceRestored(boolean instanceStateRetained) {
        if (instanceStateRetained) {
            startFabButtonAnimation();
        }
    }

    @NonNull
    @Override
    public ShotsContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    private void initComponent() {
        component = App.getUserComponent(getContext())
                .getShotsComponent(new ShotsModule(this, this, peekAndPop));
        component.inject(this);
    }

    private void initPeekAndPop() {
        peekAndPop = ShotPeekAndPop.init(getActivity(), shotsRecyclerView, this, this);
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

    @Override
    public List<Shot> getData() {
        return adapter.getData();
    }

    @Override
    public void setData(List<Shot> data) {
        adapter.setShots(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getShotsFromServer(pullToRefresh,
                getArguments().getBoolean(KEY_SHOULD_SHOW_SHOTS_ANIMATION, false));
    }

    @Override
    public void refreshFragmentData() {
        getPresenter().getShotsFromServer(false, false);
    }

    @Override
    public void showMoreItems(List<Shot> items) {
        adapter.addMoreItems(items);
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

        shotActionListener.showShotDetails(selectedShot, adapter.getShots(), request);
    }

    @Override
    public void onDetailsVisibilityChange(boolean isVisible) {
        final int currentPosition = shotsRecyclerView.getCurrentItem();
        shotsRecyclerView.setAdapter(null);

        shotsRecyclerView.setAdapter(adapter);
        adapter.setDetailsVisibilityFlag(isVisible);

        shotsRecyclerView.scrollToPosition(currentPosition);

        if (detailsVisibilityChangeListener != null)
            detailsVisibilityChangeListener.onDetailsChangeVisibility(isVisible);
    }

    @Override
    public void updateShot(Shot shot) {
        adapter.updateShotIfExists(shot);
    }

    @Override
    public void showFirstShot() {
        shotsRecyclerView.scrollToPosition(0);
    }

    @Override
    public void showShotDetails(Shot shot) {
        ShotDetailsRequest request = ShotDetailsRequest.builder()
                .detailsType(ShotDetailsType.DEFAULT)
                .build();

        shotActionListener.showShotDetails(shot, adapter.getShots(), request);
    }

    @Override
    public void onShotLikeSwipe(Shot shot) {
        getPresenter().likeShot(shot);
        analyticsEventLogger.logEventShotSwipeLike();
    }

    @Override
    public void onLikeAndAddShotToBucketSwipe(Shot shot) {
        getPresenter().likeShot(shot);
        getPresenter().handleAddShotToBucket(shot);
        analyticsEventLogger.logEventShotSwipeAddToBucket();
    }

    @Override
    public void onCommentShotSwipe(Shot shot) {
        getPresenter().showCommentInput(shot);
        analyticsEventLogger.logEventShotSwipeComment();
    }

    @Override
    public void onFollowUserSwipe(Shot shot) {
        getPresenter().handleFollowShotAuthor(shot);
        analyticsEventLogger.logEventShotSwipeFollow();
    }

    @Override
    public void onBucketForShotSelect(Bucket bucket, Shot shot) {
        getPresenter().addShotToBucket(bucket, shot);
    }

    @Override
    public void onShotSelected(Shot shot) {
        getPresenter().showShotDetails(shot);
        analyticsEventLogger.logEventShotsItemClick();
    }

    @Override
    public void onStartSwipe(Shot shot) {
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onEndSwipe(Shot shot) {
        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void showLoadingIndicator(boolean swipeToRefresh) {
        twoCoveredShotsAnimationView.setVisibility(View.GONE);
        shotsRecyclerView.setVisibility(View.GONE);
        fabMenu.setVisibility(View.GONE);
        loadingBallContainer.setVisibility(View.VISIBLE);
        ballImageView.post(() -> ballImageView.startAnimation(ballAnimation));
        ballShadowImageView.post(() -> ballShadowImageView.startAnimation(shadowAnimation));
    }

    @Override
    public void hideLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(false);
        loadingBallContainer.post(() -> loadingBallContainer.setVisibility(View.GONE));
        shotsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showShotsAnimation(@NonNull Shot firstShot, @NonNull Shot secondShot) {
        twoCoveredShotsAnimationView.setVisibility(View.VISIBLE);
        customLinearLayoutManager.setCanScrollVertically(false);
        onDetailsVisibilityChange(false);
        twoCoveredShotsAnimationView.loadShotsAndStartAnimation(firstShot, secondShot, () -> {
            getPresenter().getShotsCustomizationSettings();
            startFabButtonAnimation();
            customLinearLayoutManager.setCanScrollVertically(true);
            twoCoveredShotsAnimationView.setVisibility(View.GONE);
        });
    }


    @Override
    public void showMessageOnServerError(String errorText) {
        Snackbar.make(shotsRecyclerView, errorText, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setListener(DetailsVisibilityChangeListener listener) {
        this.detailsVisibilityChangeListener = listener;
    }

    @Override
    public void onBucketToRemoveFromForShotSelect(List<Bucket> list, Shot shot) {
        getPresenter().removeShotFromBuckets(list, shot);
    }

    @Override
    public void onShotAddedToBucket() {
        shotActionListener.onShotAddedToBucket();
    }

    @Override
    public void onUserFollowed() {
        shotActionListener.onUserFollowed();
    }

    @Override
    public void showSideMenu() {
        fabMenu.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.fab_like_menu)
    void onLikeFabClick() {
        int currentItemPosition = shotsRecyclerView.getCurrentItem();
        if (currentItemPosition != RecyclerView.NO_POSITION) {
            getPresenter().likeShot(adapter.getShotFromPosition(currentItemPosition));
        }
        analyticsEventLogger.logEventShotsFABLike();
    }

    @OnClick(R.id.fab_bucket_menu)
    void onBucketClick() {
        int currentItemPosition = shotsRecyclerView.getCurrentItem();
        if (currentItemPosition != RecyclerView.NO_POSITION) {
            getPresenter().handleAddShotToBucket(
                    adapter.getShotFromPosition(currentItemPosition));
        }
        analyticsEventLogger.logEventShotsFABAddToBucket();
    }

    @OnClick(R.id.fab_comment_menu)
    void onCommentClick() {
        int currentItemPosition = shotsRecyclerView.getCurrentItem();
        if (currentItemPosition != RecyclerView.NO_POSITION) {
            getPresenter()
                    .showCommentInput(adapter.getShotFromPosition(currentItemPosition));
        }
        analyticsEventLogger.logEventShotsFABComment();
    }

    @Override
    public void onShotLiked() {
        Snackbar.make(getView(), R.string.shot_liked, Snackbar.LENGTH_LONG).show();
        shotActionListener.shotLikeStatusChanged();

    }

    @OnClick(R.id.fab_follow_menu)
    void onFollowClick() {
        int currentItemPosition = shotsRecyclerView.getCurrentItem();
        if (currentItemPosition != RecyclerView.NO_POSITION) {
            getPresenter()
                    .handleFollowShotAuthor(adapter.getShotFromPosition(currentItemPosition));
        }
        analyticsEventLogger.logEventShotsFABFollow();
    }

    private void initFabMenu() {
        fabMenu.setOrientation(getResources().getConfiguration().orientation);
        fabMenu.addFogView(fogContainerView);
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.accent));
        swipeRefreshLayout.setOnRefreshListener(() -> getPresenter().getShotsFromServer(true, false));
    }

    private void initRecycler() {
        customLinearLayoutManager = new CustomLinearLayoutManager(getContext());
        shotsRecyclerView.setAdapter(adapter);
        shotsRecyclerView.setLayoutManager(customLinearLayoutManager);
        shotsRecyclerView.setHasFixedSize(true);
        shotsRecyclerView.addOnScrollListener(new LoadMoreScrollListener(SHOTS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                getPresenter().getMoreShotsFromServer();
                analyticsEventLogger.logEventShotsListSwipes(SHOTS_TO_LOAD_MORE);
            }
        });
    }

    private void startFabButtonAnimation() {
        final Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        animation.setAnimationListener(new OnAnimationEndListener(() -> fabMenu.setVisibility(View.VISIBLE)));
        fabMenu.startAnimation(animation);
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

    @Override
    public void onPeek(View view, int i) {
        peekAndPop.bindPeekAndPop(adapter.getData().get(i));
        shotsRecyclerView.requestDisallowInterceptTouchEvent(true);
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onPop(View view, int i) {
        swipeRefreshLayout.setEnabled(true);
    }

    public interface ShotActionListener {
        void shotLikeStatusChanged();

        void showShotDetails(Shot shot, List<Shot> nearbyShots, ShotDetailsRequest detailsRequest);

        void onShotAddedToBucket();

        void onUserFollowed();
    }

}
