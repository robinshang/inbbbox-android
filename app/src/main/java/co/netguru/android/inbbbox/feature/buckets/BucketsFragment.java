package co.netguru.android.inbbbox.feature.buckets;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.buckets.adapter.BaseBucketViewHolder;
import co.netguru.android.inbbbox.feature.buckets.adapter.BucketsAdapter;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragmentWithWithListTypeSelection;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.utils.TextFormatterUtil;
import co.netguru.android.inbbbox.view.LoadMoreScrollListener;

public class BucketsFragment extends BaseMvpFragmentWithWithListTypeSelection<BucketsFragmentContract.View, BucketsFragmentContract.Presenter>
        implements BucketsFragmentContract.View, BaseBucketViewHolder.BucketClickListener {

    @BindDrawable(R.drawable.ic_following_emptystate)
    Drawable emptyTextDrawable;

    @BindString(R.string.fragment_buckets_empty_text_before_icon)
    String emptyStringBeforeIcon;

    @BindString(R.string.fragment_buckets_empty_text_after_icon)
    String emptyStringAfterIcon;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.empty_view)
    ScrollView emptyView;
    @BindView(R.id.fragment_followers_empty_text)
    TextView emptyViewText;
    @BindView(R.id.buckets_recycler_view)
    RecyclerView bucketsRecyclerView;

    private static final int SPAN_COUNT = 2;
    private static final int LAST_X_BUCKETS_VISIBLE_TO_LOAD_MORE = 5;

    private final BucketsAdapter adapter = new BucketsAdapter(this);
    private final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
    private final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

    public static BucketsFragment newInstance() {
        return new BucketsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buckets, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initRefreshLayout();
        initEmptyView();
        getPresenter().loadBucketsWithShots(false);
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        adapter.setGridMode(isGridMode);
        bucketsRecyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
    }

    @Override
    public BucketsFragmentContract.Presenter createPresenter() {
        return App.getAppComponent(getContext()).inject().getPresenter();
    }

    @Override
    public void onBucketClick(BucketWithShots bucketWithShots) {
        //// TODO: 21.11.2016 not in scope of this task
    }

    @Override
    public void showBucketsWithShots(List<BucketWithShots> bucketsWithShots) {
        bucketsRecyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        adapter.setNewBucketWithShots(bucketsWithShots);
    }

    @Override
    public void hideProgressBars() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void addMoreBucketsWithShots(List<BucketWithShots> bucketWithShotsList) {
        adapter.addNewBucketWithShots(bucketWithShotsList);
    }

    @Override
    public void showEmptyBucketView() {
        emptyView.setVisibility(View.VISIBLE);
        bucketsRecyclerView.setVisibility(View.GONE);
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.accent));
        swipeRefreshLayout.setOnRefreshListener(() -> getPresenter().loadBucketsWithShots(true));
    }

    private void initRecyclerView() {
        bucketsRecyclerView.setHasFixedSize(true);
        bucketsRecyclerView.setAdapter(adapter);
        bucketsRecyclerView.addOnScrollListener(new LoadMoreScrollListener(LAST_X_BUCKETS_VISIBLE_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                presenter.loadMoreBucketsWithShots();
            }
        });
    }

    private void initEmptyView() {
        emptyViewText.setText(TextFormatterUtil
                .addDrawableBeetweenStrings(emptyStringBeforeIcon, emptyStringAfterIcon, emptyTextDrawable));
    }
}
