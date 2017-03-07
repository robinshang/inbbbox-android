package co.netguru.android.inbbbox.feature.bucket.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.peekandpop.shalskar.peekandpop.PeekAndPop;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.analytics.AnalyticsEventLogger;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.common.utils.TextFormatterUtil;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpLceFragmentWithListTypeSelection;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;
import co.netguru.android.inbbbox.feature.shared.shotsadapter.SharedShotsAdapter;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import co.netguru.android.inbbbox.feature.shot.addtobucket.AddToBucketDialogFragment;
import timber.log.Timber;

public class BucketDetailsFragment extends BaseMvpLceFragmentWithListTypeSelection
        <SwipeRefreshLayout, List<Shot>,  BucketDetailsContract.View, BucketDetailsContract.Presenter>
        implements BucketDetailsContract.View, DeleteBucketDialogFragment.DeleteBucketDialogListener,
        AddToBucketDialogFragment.BucketSelectListener, PeekAndPop.OnGeneralActionListener {

    public static final String TAG = BucketDetailsFragment.class.getSimpleName();

    public static final String DELETED_BUCKET_ID_KEY = "deleted_bucket_id_key";

    private static final int LAST_X_SHOTS_VISIBLE_TO_LOAD_MORE = 10;
    private static final String BUCKET_WITH_SHOTS_ARG_KEY = "bucket_with_shots_arg_key";
    private static final String SHOTS_PER_PAGE_ARG_KEY = "shots_per_page_arg_key";
    private static final int SPAN_COUNT = 2;
    @BindDrawable(R.drawable.ic_buckets_empty_state)
    Drawable emptyTextDrawable;
    @BindString(R.string.fragment_bucket_is_empty_text_before_icon)
    String emptyStringBeforeIcon;
    @BindString(R.string.fragment_bucket_is_empty_text_after_icon)
    String emptyStringAfterIcon;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.empty_view)
    ScrollView emptyView;
    @BindView(R.id.fragment_buckets_empty_text)
    TextView emptyViewText;
    @Inject
    AnalyticsEventLogger analyticsEventLogger;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private Snackbar loadingMoreSnackbar;
    private SharedShotsAdapter bucketShotsAdapter;
    private BucketsDetailsComponent component;
    private ShotPeekAndPop peekAndPop;

    private ShotClickListener shotClickListener;

    public static BucketDetailsFragment newInstance(BucketWithShots bucketWithShots, int perPage) {

        Bundle args = new Bundle();
        args.putParcelable(BUCKET_WITH_SHOTS_ARG_KEY, bucketWithShots);
        args.putInt(SHOTS_PER_PAGE_ARG_KEY, perPage);

        BucketDetailsFragment fragment = new BucketDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            shotClickListener = (ShotClickListener) context;
        } catch (ClassCastException e) {
            throw new InterfaceNotImplementedException(e, context.toString(), ShotClickListener.class.getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initPeekAndPop();
        initComponent();
        analyticsEventLogger.logEventScreenBucketDetails();
        return inflater.inflate(R.layout.fragment_bucket_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        initRefreshLayout();
        initEmptyView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loadingMoreSnackbar = null;
    }

    private void initComponent() {
        component = App.getUserComponent(getContext()).plusBucketDetailsComponent();
        component.inject(this);
    }

    private void initPeekAndPop() {
        peekAndPop = ShotPeekAndPop.init(getActivity(), recyclerView, this, this);
    }

    @NonNull
    @Override
    public BucketDetailsContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @NonNull
    @Override
    public LceViewState<List<Shot>, BucketDetailsContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Shot> getData() {
        return bucketShotsAdapter.getData();
    }

    @Override
    public void setData(List<Shot> data) {
        bucketShotsAdapter.setShots(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        final BucketWithShots bucketWithShots = getArguments().getParcelable(BUCKET_WITH_SHOTS_ARG_KEY);
        final int perPage = getArguments().getInt(SHOTS_PER_PAGE_ARG_KEY);
        getPresenter().handleBucketData(bucketWithShots, perPage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_bucket:
                getPresenter().onDeleteBucketClick();
                analyticsEventLogger.logEventAppbarDeleteBucket();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        recyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
        bucketShotsAdapter.setGridMode(isGridMode);
        analyticsEventLogger.logEventAppbarCollectionLayoutChange(isGridMode);
    }

    @Override
    public void setFragmentTitle(String title) {
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void showLoadingMoreShotsView() {
        if (loadingMoreSnackbar == null && getView() != null) {
            loadingMoreSnackbar = Snackbar.make(getView(), R.string.loading_more_shots, Snackbar.LENGTH_INDEFINITE);
        }
        loadingMoreSnackbar.show();
    }

    @Override
    public void hideLoadingMoreShotsView() {
        if (loadingMoreSnackbar != null) {
            loadingMoreSnackbar.dismiss();
        }
    }

    @Override
    public void showContent() {
        super.showContent();
        getPresenter().checkDataEmpty(getData());
    }

    @Override
    public void addShots(List<Shot> shots) {
        bucketShotsAdapter.addNewShots(shots);
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyView() {
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showRemoveBucketDialog(@NonNull String bucketName) {
        DeleteBucketDialogFragment.newInstance(this, bucketName)
                .show(getFragmentManager(), DeleteBucketDialogFragment.TAG);
    }

    @Override
    public void showRefreshedBucketsView(long deletedBucketId) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(DELETED_BUCKET_ID_KEY, deletedBucketId);
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
        getActivity().finish();
    }

    @Override
    public void showBucketAddSuccess() {
        showTextOnSnackbar(R.string.shots_fragment_add_shot_to_bucket_success);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        showTextOnSnackbar(errorText);
    }

    private void setupRecyclerView() {
        gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        linearLayoutManager = new LinearLayoutManager(getContext());
        bucketShotsAdapter = new SharedShotsAdapter(shotClickListener, peekAndPop);
        recyclerView.setAdapter(bucketShotsAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(
                new LoadMoreScrollListener(LAST_X_SHOTS_VISIBLE_TO_LOAD_MORE) {
                    @Override
                    public void requestMoreData() {
                        presenter.loadMoreShots();
                    }
                });
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.accent));
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::refreshShots);
    }

    private void initEmptyView() {
        int lineHeight = emptyViewText.getLineHeight();
        //noinspection SuspiciousNameCombination
        emptyTextDrawable.setBounds(0, 0, lineHeight, lineHeight);
        emptyViewText.setText(TextFormatterUtil
                .addDrawableBetweenStrings(emptyStringBeforeIcon, emptyStringAfterIcon, emptyTextDrawable));
    }

    @Override
    public void onDeleteBucket() {
        getPresenter().deleteBucket();
    }

    @Override
    public void onBucketForShotSelect(Bucket bucket, Shot shot) {
        peekAndPop.onBucketForShotSelect(bucket, shot);
    }

    @Override
    public void onPeek(View view, int i) {
        peekAndPop.bindPeekAndPop(bucketShotsAdapter.getData().get(i));
        recyclerView.requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void onPop(View view, int i) {
        // no-op
    }

}