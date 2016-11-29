package co.netguru.android.inbbbox.feature.buckets.details;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.buckets.details.adapter.BucketShotViewHolder;
import co.netguru.android.inbbbox.feature.buckets.details.adapter.BucketShotsAdapter;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragmentWithWithListTypeSelection;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.view.LoadMoreScrollListener;

public class BucketDetailsFragment
        extends BaseMvpFragmentWithWithListTypeSelection<BucketDetailsContract.View, BucketDetailsContract.Presenter>
        implements BucketDetailsContract.View, BucketShotViewHolder.OnShotInBucketClickListener {

    public static final String TAG = BucketDetailsFragment.class.getSimpleName();
    private static final int LAST_X_SHOTS_VISIBLE_TO_LOAD_MORE = 5;

    private static final String BUCKET_WITH_SHOTS_ARG_KEY = "bucket_with_shots_arg_key";
    private static final String SHOTS_PER_PAGE_ARG_KEY = "shots_per_page_arg_key";
    private static final int SPAN_COUNT = 2;

    private final BucketShotsAdapter bucketShotsAdapter = new BucketShotsAdapter(this);
    private final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
    private final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private Snackbar loadingMoreSnackbar;

    @BindView(R.id.shots_from_bucket_recycler_view)
    RecyclerView recyclerView;

    public static BucketDetailsFragment newInstance(BucketWithShots bucketWithShots, int perPage) {

        Bundle args = new Bundle();
        args.putParcelable(BUCKET_WITH_SHOTS_ARG_KEY, bucketWithShots);
        args.putInt(SHOTS_PER_PAGE_ARG_KEY, perPage);

        BucketDetailsFragment fragment = new BucketDetailsFragment();
        fragment.setArguments(args);
        return fragment;
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
        BucketWithShots bucketWithShots = getArguments().getParcelable(BUCKET_WITH_SHOTS_ARG_KEY);
        int perPage = getArguments().getInt(SHOTS_PER_PAGE_ARG_KEY);
        getPresenter().handleBucketData(bucketWithShots, perPage);
    }

    @Override
    public BucketDetailsContract.Presenter createPresenter() {
        return App.getAppComponent(getContext()).plusBucketDetailsComponent().getPresenter();
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        recyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
        bucketShotsAdapter.setGridMode(isGridMode);
    }


    @Override
    public void setFragmentTitle(Bucket bucket) {
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(bucket.name());
        }
    }

    @Override
    public void setShots(List<ShotEntity> shotEntities) {
        bucketShotsAdapter.setNewShots(shotEntities);
    }

    @Override
    public void onShotClick(ShotEntity shotEntity) {

    }

    private void setupRecyclerView() {
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
}
