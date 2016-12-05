package co.netguru.android.inbbbox.feature.buckets.details;


import android.content.Context;
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

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.buckets.details.adapter.BucketShotViewHolder;
import co.netguru.android.inbbbox.feature.buckets.details.adapter.BucketShotsAdapter;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragmentWithWithListTypeSelection;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.utils.TextFormatterUtil;
import co.netguru.android.inbbbox.view.LoadMoreScrollListener;

public class BucketDetailsFragment
        extends BaseMvpFragmentWithWithListTypeSelection<BucketDetailsContract.View, BucketDetailsContract.Presenter>
        implements BucketDetailsContract.View, DeleteBucketDialogFragment.DeleteBucketDialogListener {

    public static final String TAG = BucketDetailsFragment.class.getSimpleName();
    private static final int LAST_X_SHOTS_VISIBLE_TO_LOAD_MORE = 10;

    private static final String BUCKET_WITH_SHOTS_ARG_KEY = "bucket_with_shots_arg_key";
    private static final String SHOTS_PER_PAGE_ARG_KEY = "shots_per_page_arg_key";
    private static final int SPAN_COUNT = 2;

    private final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
    private final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

    @BindDrawable(R.drawable.ic_buckets_empty_state)
    Drawable emptyTextDrawable;
    @BindString(R.string.fragment_bucket_is_empty_text_before_icon)
    String emptyStringBeforeIcon;
    @BindString(R.string.fragment_bucket_is_empty_text_after_icon)
    String emptyStringAfterIcon;

    @BindView(R.id.shots_from_bucket_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.empty_view)
    ScrollView emptyView;
    @BindView(R.id.fragment_buckets_empty_text)
    TextView emptyViewText;

    private Snackbar loadingMoreSnackbar;
    private BucketShotsAdapter bucketShotsAdapter;

    private BucketShotViewHolder.OnShotInBucketClickListener shotInBucketClickListener;

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
            shotInBucketClickListener =
                    (BucketShotViewHolder.OnShotInBucketClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BucketDetailsFragmentActionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bucket_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        initRefreshLayout();
        initEmptyView();
        BucketWithShots bucketWithShots = getArguments().getParcelable(BUCKET_WITH_SHOTS_ARG_KEY);
        int perPage = getArguments().getInt(SHOTS_PER_PAGE_ARG_KEY);
        getPresenter().handleBucketData(bucketWithShots, perPage);
    }

    @NonNull
    @Override
    public BucketDetailsContract.Presenter createPresenter() {
        return App.getAppComponent(getContext()).plusBucketDetailsComponent().getPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_bucket:
                getPresenter().onDeleteBucketClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        recyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
        bucketShotsAdapter.setGridMode(isGridMode);
    }


    @Override
    public void setFragmentTitle(String title) {
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void showShots(List<ShotEntity> shotEntities) {
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        bucketShotsAdapter.setNewShots(shotEntities);
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
    public void addShots(List<ShotEntity> shotEntities) {
        bucketShotsAdapter.addNewShots(shotEntities);
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
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
    public void showError(String message) {
        showTextOnSnackbar(message);
    }

    @Override
    public void showRefreshedBucketsView() {
        getActivity().setResult(BucketDetailsActivity.BUCKET_DELETED_RESULT_KEY);
        getActivity().finish();
    }

    private void setupRecyclerView() {
        bucketShotsAdapter = new BucketShotsAdapter(shotInBucketClickListener);
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

}