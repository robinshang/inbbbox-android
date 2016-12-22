package co.netguru.android.inbbbox.feature.buckets;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.buckets.adapter.BaseBucketViewHolder;
import co.netguru.android.inbbbox.feature.buckets.adapter.BucketsAdapter;
import co.netguru.android.inbbbox.feature.buckets.createbucket.CreateBucketDialogFragment;
import co.netguru.android.inbbbox.feature.buckets.details.BucketDetailsActivity;
import co.netguru.android.inbbbox.feature.buckets.details.BucketDetailsFragment;
import co.netguru.android.inbbbox.feature.common.BaseMvpLceFragmentWithListTypeSelection;
import co.netguru.android.inbbbox.feature.main.adapter.RefreshableFragment;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.utils.TextFormatterUtil;
import co.netguru.android.inbbbox.view.LoadMoreScrollListener;
import onactivityresult.ActivityResult;
import onactivityresult.OnActivityResult;
import timber.log.Timber;

public class BucketsFragment extends BaseMvpLceFragmentWithListTypeSelection<SwipeRefreshLayout,
        List<BucketWithShots>, BucketsFragmentContract.View, BucketsFragmentContract.Presenter>
        implements RefreshableFragment, BucketsFragmentContract.View, BaseBucketViewHolder.BucketClickListener {

    private static final int BUCKET_DETAILS_VIEW_REQUEST_CODE = 1;
    private static final int SPAN_COUNT = 2;
    private static final int LAST_X_BUCKETS_VISIBLE_TO_LOAD_MORE = 5;

    @BindDrawable(R.drawable.ic_buckets_empty_state)
    Drawable emptyTextDrawable;

    @BindString(R.string.fragment_buckets_empty_text_before_icon)
    String emptyStringBeforeIcon;
    @BindString(R.string.fragment_buckets_empty_text_after_icon)
    String emptyStringAfterIcon;

    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.loadingView)
    ProgressBar progressBar;
    @BindView(R.id.empty_view)
    ScrollView emptyView;
    @BindView(R.id.fragment_buckets_empty_text)
    TextView emptyViewText;
    @BindView(R.id.buckets_recycler_view)
    RecyclerView bucketsRecyclerView;

    private BucketsAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private Snackbar loadingMoreSnackbar;

    public static BucketsFragment newInstance() {
        return new BucketsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buckets, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEmptyView();
        initRecyclerView();
        initRefreshLayout();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loadingMoreSnackbar = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ActivityResult.onResult(requestCode, resultCode, data).into(this);
    }

    @OnActivityResult(requestCode = BUCKET_DETAILS_VIEW_REQUEST_CODE,
            resultCodes = Activity.RESULT_OK)
    public void onActivityResultBucketDeleted(Intent data) {
        long deletedBucketId =
                data.getLongExtra(BucketDetailsFragment.DELETED_BUCKET_ID_KEY, Constants.UNDEFINED);
        if (deletedBucketId != Constants.UNDEFINED) {
            getPresenter().handleDeleteBucket(deletedBucketId);
        } else {
            Timber.e("Bucket delete request was success, but no bucket id was passed.");
        }
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        adapter.setGridMode(isGridMode);
        bucketsRecyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
    }

    @NonNull
    @Override
    public BucketsFragmentContract.Presenter createPresenter() {
        return App.getAppComponent(getContext()).plusBucketsFragmentComponent().getPresenter();
    }

    @NonNull
    @Override
    public LceViewState<List<BucketWithShots>, BucketsFragmentContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public void showContent() {
        super.showContent();
        getPresenter().checkEmptyData(getData());
    }

    @Override
    public List<BucketWithShots> getData() {
        return adapter.getData();
    }

    @Override
    public void setData(List<BucketWithShots> data) {
        adapter.setNewBucketsWithShots(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadBucketsWithShots();
    }

    @Override
    public void onBucketClick(BucketWithShots bucketWithShots) {
        getPresenter().handleBucketWithShotsClick(bucketWithShots);
    }

    @Override
    public void hideProgressBars() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void addMoreBucketsWithShots(List<BucketWithShots> bucketWithShotsList) {
        adapter.addNewBucketsWithShots(bucketWithShotsList);
    }

    @Override
    public void hideEmptyBucketView() {
        emptyView.setVisibility(View.GONE);
        bucketsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyBucketView() {
        emptyView.setVisibility(View.VISIBLE);
        bucketsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingMoreBucketsView() {
        if (loadingMoreSnackbar == null && getView() != null) {
            loadingMoreSnackbar = Snackbar.make(getView(), R.string.loading_more_buckets, Snackbar.LENGTH_INDEFINITE);
        }
        loadingMoreSnackbar.show();
    }

    @Override
    public void hideLoadingMoreBucketsView() {
        if (loadingMoreSnackbar != null) {
            loadingMoreSnackbar.dismiss();
        }
    }

    @Override
    public void showDetailedBucketView(BucketWithShots bucketWithShots, int bucketShotsPerPageCount) {
        BucketDetailsActivity.startActivityForDeleteResult(this, getContext(), BUCKET_DETAILS_VIEW_REQUEST_CODE,
                bucketWithShots, bucketShotsPerPageCount);
    }

    @Override
    public void openCreateDialogFragment() {
        CreateBucketDialogFragment
                .newInstance()
                .show(getFragmentManager(), CreateBucketDialogFragment.TAG);
    }

    @Override
    public void addNewBucketWithShotsOnTop(BucketWithShots bucketWithShots) {
        bucketsRecyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        adapter.addNewBucketWithShots(bucketWithShots);
        hideEmptyBucketView();
    }

    @Override
    public void scrollToTop() {
        bucketsRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void removeBucketFromList(long bucketId) {
        adapter.removeBucketWithGivenId(bucketId);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        Snackbar.make(bucketsRecyclerView, errorText, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.create_bucket_fab)
    public void onCreateBucketFabClick() {
        getPresenter().handleCreateBucket();
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.accent));
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::loadBucketsWithShots);
    }

    @Override
    public void refreshFragmentData() {
        swipeRefreshLayout.setRefreshing(true);
        getPresenter().loadBucketsWithShots();
    }

    private void initRecyclerView() {
        adapter = new BucketsAdapter(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        bucketsRecyclerView.setHasFixedSize(true);
        bucketsRecyclerView.setAdapter(adapter);
        bucketsRecyclerView.addOnScrollListener(
                new LoadMoreScrollListener(LAST_X_BUCKETS_VISIBLE_TO_LOAD_MORE) {
                    @Override
                    public void requestMoreData() {
                        presenter.loadMoreBucketsWithShots();
                    }
                });
    }

    private void initEmptyView() {
        int lineHeight = emptyViewText.getLineHeight();
        //noinspection SuspiciousNameCombination
        emptyTextDrawable.setBounds(0, 0, lineHeight, lineHeight);
        emptyViewText.setText(TextFormatterUtil
                .addDrawableBetweenStrings(emptyStringBeforeIcon, emptyStringAfterIcon, emptyTextDrawable));
    }
}