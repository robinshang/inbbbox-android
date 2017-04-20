package co.netguru.android.inbbbox.feature.user.buckets;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.bucket.detail.BucketDetailsActivity;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpViewStateFragment;
import co.netguru.android.inbbbox.feature.shared.collectionadapter.CollectionAdapter;
import co.netguru.android.inbbbox.feature.shared.collectionadapter.CollectionClickListener;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import co.netguru.android.inbbbox.feature.user.info.team.ShotActionListener;
import timber.log.Timber;

public class UserBucketsFragment extends BaseMvpViewStateFragment<SwipeRefreshLayout,
        List<BucketWithShots>, UserBucketsContract.View, UserBucketsContract.Presenter>
        implements UserBucketsContract.View, CollectionAdapter.OnGetMoreCollectionShotsListener<BucketWithShots>,
        CollectionClickListener<BucketWithShots> {

    private static final int BUCKET_DETAILS_VIEW_REQUEST_CODE = 1;
    private static final int LAST_X_BUCKETS_VISIBLE_TO_LOAD_MORE = 5;
    private static final String KEY_USER = "key:user";

    @BindView(R.id.buckets_recycler_view)
    RecyclerView bucketsRecyclerView;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.loadingView)
    ProgressBar progressBar;
    private CollectionAdapter<BucketWithShots> adapter;
    private Snackbar loadingMoreSnackbar;
    private ShotActionListener shotActionListener;

    public static UserBucketsFragment newInstance(User user) {
        UserBucketsFragment fragment = new UserBucketsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_USER, user);

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

    @Override
    public void onDetach() {
        super.onDetach();
        shotActionListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buckets_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initRefreshLayout();
    }

    @Override
    public LceViewState<List<BucketWithShots>, UserBucketsContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<BucketWithShots> getData() {
        return adapter.getData();
    }

    @Override
    public void setData(List<BucketWithShots> bucketsWithShotsList) {
        adapter.setCollectionsList(bucketsWithShotsList);
    }

    @Override
    public UserBucketsContract.Presenter createPresenter() {
        UserBucketsModule module = new UserBucketsModule(getArguments().getParcelable(KEY_USER));
        return App.getUserComponent(
                getContext()).plusUserBucketsComponent(module).getPresenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadBucketsWithShots(!pullToRefresh);
    }

    private LoadMoreScrollListener getLoadMoreScrollListener() {
        return new LoadMoreScrollListener(LAST_X_BUCKETS_VISIBLE_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                presenter.loadMoreBucketsWithShots();
            }
        };
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        Snackbar.make(bucketsRecyclerView, errorText, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingMoreBucketsView() {
        if (loadingMoreSnackbar == null && getView() != null) {
            loadingMoreSnackbar = Snackbar.make(getView(), R.string.loading_more_buckets, Snackbar.LENGTH_INDEFINITE);
        }
        loadingMoreSnackbar.show();
    }

    @Override
    public void hideProgressBars() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void hideLoadingMoreBucketsView() {
        if (loadingMoreSnackbar != null) {
            loadingMoreSnackbar.dismiss();
        }
    }

    @Override
    public void addMoreBucketsWithShots(List<BucketWithShots> bucketsWithShotsList) {
        adapter.addMoreCollections(bucketsWithShotsList);
    }

    @Override
    public void showDetailedBucketView(BucketWithShots bucketWithShots, int bucketShotsPerPageCount) {
        BucketDetailsActivity.startActivityForDeleteResult(this, getContext(), BUCKET_DETAILS_VIEW_REQUEST_CODE,
                bucketWithShots, bucketShotsPerPageCount);
    }

    @Override
    public void showContent() {
        super.showContent();
        presenter.showContentForData(getData());
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        bucketsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyView() {
        emptyView.setVisibility(View.GONE);
        bucketsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showShotDetails(Shot shot, List<Shot> allShots) {
        shotActionListener.showShotDetails(shot, allShots, shot.author().id());
    }

    @Override
    public void addMoreBucketShots(long bucketId, List<Shot> newShots, int shotsPerPage) {
        adapter.addMoreCollectionShots(bucketId, newShots, shotsPerPage);
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.accent));
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::refreshBuckets);
    }

    private void initRecyclerView() {
        adapter = new CollectionAdapter<>(this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        bucketsRecyclerView.setHasFixedSize(true);
        bucketsRecyclerView.setLayoutManager(linearLayoutManager);
        bucketsRecyclerView.setAdapter(adapter);
        bucketsRecyclerView.addOnScrollListener(getLoadMoreScrollListener());
    }

    @Override
    public void onCollectionClick(BucketWithShots bucketWithShots) {
        getPresenter().handleBucketWithShotsClick(bucketWithShots);
    }

    @Override
    public void onShotClick(Shot shot, BucketWithShots bucketWithShots) {
        getPresenter().onShotClick(shot, bucketWithShots);
    }

    @Override
    public void onGetMoreCollectionShots(BucketWithShots bucketWithShots) {
        getPresenter().getMoreShotsFromBucket(bucketWithShots);
    }
}