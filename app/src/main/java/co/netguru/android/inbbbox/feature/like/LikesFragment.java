package co.netguru.android.inbbbox.feature.like;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.peekandpop.shalskar.peekandpop.PeekAndPop;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.analytics.AnalyticsEventLogger;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.common.utils.TextFormatterUtil;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.like.adapter.LikesAdapter;
import co.netguru.android.inbbbox.feature.main.adapter.RefreshableFragment;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpLceFragmentWithListTypeSelection;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import co.netguru.android.inbbbox.feature.shot.ShotsFragment;
import co.netguru.android.inbbbox.feature.shot.addtobucket.AddToBucketDialogFragment;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsType;

public class LikesFragment extends BaseMvpLceFragmentWithListTypeSelection<SwipeRefreshLayout, List<Shot>,
        LikesViewContract.View, LikesViewContract.Presenter> implements RefreshableFragment,
        LikesViewContract.View, ShotClickListener, PeekAndPop.OnGeneralActionListener,
        AddToBucketDialogFragment.BucketSelectListener {

    private static final int GRID_VIEW_COLUMN_COUNT = 2;
    private static final int LIKES_TO_LOAD_MORE = 10;

    @BindDrawable(R.drawable.ic_like_emptystate)
    Drawable emptyTextDrawable;

    @BindString(R.string.fragment_like_empty_text)
    String emptyString;

    @BindString(R.string.error_server)
    String errorString;

    @BindColor(R.color.accent)
    int accentColor;

    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_likes_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_likes_empty_view)
    ScrollView emptyView;
    @BindView(R.id.fragment_like_empty_text)
    TextView emptyViewText;
    @BindView(R.id.loadingView)
    ProgressBar progressBar;
    @BindView(R.id.errorLayout)
    LinearLayout errorLayout;
    @Inject
    AnalyticsEventLogger analyticsEventLogger;
    private ShotPeekAndPop peekAndPop;
    private Snackbar loadingMoreSnackbar;
    private LikesAdapter likesAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private ShotsFragment.ShotActionListener shotActionListener;
    private LikesFragmentComponent component;

    public static LikesFragment newInstance() {
        return new LikesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            shotActionListener = (ShotsFragment.ShotActionListener) context;
        } catch (ClassCastException e) {
            throw new InterfaceNotImplementedException(e, context.toString(),
                    ShotsFragment.ShotActionListener.class.getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initPeekAndPop();
        initComponent();
        return inflater.inflate(R.layout.fragment_likes, container, false);
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
    @NonNull
    @Override
    public LikesViewContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @Override
    public void showContent() {
        super.showContent();
        errorLayout.setVisibility(View.INVISIBLE);
        getPresenter().checkDataEmpty(getData().isEmpty());
    }

    @NonNull
    @Override
    public LceViewState<List<Shot>, LikesViewContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Shot> getData() {
        return likesAdapter.getData();
    }

    @Override
    public void setData(List<Shot> data) {
        likesAdapter.setLikeList(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().getLikesFromServer();
    }

    @Override
    public void showMoreLikes(List<Shot> likedShotList) {
        likesAdapter.addMoreLikes(likedShotList);
    }

    @Override
    public void showLikeAtTop(Shot likedShot) {
        likesAdapter.addLikeOnTop(likedShot);
    }

    @Override
    public void removeShotFromLikes(Shot unlikedShot) {
        likesAdapter.removeLike(unlikedShot);
    }

    @Override
    public void showLoadingMoreLikesView() {
        if (loadingMoreSnackbar == null && getView() != null) {
            loadingMoreSnackbar = Snackbar.make(getView(), R.string.loading_more_likes, Snackbar.LENGTH_INDEFINITE);
        }
        loadingMoreSnackbar.show();
    }

    @Override
    public void hideLoadingMoreLikesView() {
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
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showEmptyLikesInfo() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void openShowDetailsScreen(Shot shot, List<Shot> shotList) {
        ShotDetailsRequest request = ShotDetailsRequest.builder()
                .detailsType(ShotDetailsType.LIKES)
                .build();

        shotActionListener.showShotDetails(shot, shotList, request);
    }

    @Override
    public void refreshFragmentData() {
        getPresenter().getLikesFromServer();
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        showError(null, false);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        errorLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return errorString;
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        recyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
        analyticsEventLogger.logEventAppbarCollectionLayoutChange(isGridMode);
    }

    @OnClick(R.id.refreshButton)
    void onRefreshButtonClick() {
        showLoading(false);
        loadData(true);
    }

    @Override
    public void onShotClick(Shot shot) {
        getPresenter().showShotDetails(shot, likesAdapter.getData());
        analyticsEventLogger.logEventLikesItemClick();
    }

    @Override
    public void onPeek(View view, int i) {
        peekAndPop.bindPeekAndPop(likesAdapter.getData().get(i));
        recyclerView.requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void onPop(View view, int i) {
        // no-op
    }

    @Override
    public void onBucketForShotSelect(Bucket bucket, Shot shot) {
        peekAndPop.onBucketForShotSelect(bucket, shot);

    }

    private void initComponent() {
        component = App.getUserComponent(getContext()).getLikesFragmentComponent();
        component.inject(this);
    }

    private void initEmptyView() {
        int lineHeight = emptyViewText.getLineHeight();
        emptyTextDrawable.setBounds(0, 0, lineHeight, lineHeight);
        emptyViewText.setText(TextFormatterUtil
                .addDrawableToTextAtFirstSpace(emptyString, emptyTextDrawable), TextView.BufferType.SPANNABLE);
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(accentColor);
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::getLikesFromServer);
    }

    private void initRecyclerView() {
        likesAdapter = new LikesAdapter(this, peekAndPop);
        linearLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager = new GridLayoutManager(getContext(), GRID_VIEW_COLUMN_COUNT);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(likesAdapter);
        recyclerView.addOnScrollListener(new LoadMoreScrollListener(LIKES_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                presenter.getMoreLikesFromServer();
            }
        });
    }

    private void initPeekAndPop() {
        peekAndPop = ShotPeekAndPop.init(getActivity(), recyclerView, this, this);
    }
}