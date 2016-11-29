package co.netguru.android.inbbbox.feature.buckets.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.buckets.details.adapter.BucketShotViewHolder;
import co.netguru.android.inbbbox.feature.common.BaseActivity;
import co.netguru.android.inbbbox.feature.details.ShotDetailsFragment;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.model.ui.Shot;

public class BucketDetailsActivity extends BaseActivity
        implements BucketShotViewHolder.OnShotInBucketClickListener {

    private static final String BUCKET_WITH_SHOTS_KEY = "bucket_with_shots_key";
    private static final String SHOTS_PER_PAGE_KEY = "shots_per_page_key";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindColor(R.color.white)
    int colorWhite;

    @BindView(R.id.fragment_container)
    View bottomSheetView;

    private BottomSheetBehavior<View> bottomSheetBehavior;

    public static void startActivity(Context context, BucketWithShots bucketWithShots, int perPage) {
        final Intent intent = new Intent(context, BucketDetailsActivity.class);
        intent.putExtra(BUCKET_WITH_SHOTS_KEY, bucketWithShots);
        intent.putExtra(SHOTS_PER_PAGE_KEY, perPage);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_details);
        initializeToolbar();
        initializeBottomSheet();
        if (savedInstanceState == null) {
            BucketWithShots bucketWithShots = getIntent().getParcelableExtra(BUCKET_WITH_SHOTS_KEY);
            int perPage = getIntent().getIntExtra(SHOTS_PER_PAGE_KEY, Constants.UNDEFINED);
            if (bucketWithShots != null && perPage != Constants.UNDEFINED) {
                replaceFragment(R.id.bucket_details_fragment_container,
                        BucketDetailsFragment.newInstance(bucketWithShots, perPage), BucketDetailsFragment.TAG).commit();
            } else {
                throw new IllegalArgumentException("Error shots with buckets or per page count is empty");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (isBottomSheetOpen()) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        super.onBackPressed();
    }

    private void initializeBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
    }

    @Override
    public void onShotClick(ShotEntity shotEntity) {
        Fragment fragment = ShotDetailsFragment.newInstance(Shot.create(shotEntity));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, ShotDetailsFragment.TAG)
                .commit();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private boolean isBottomSheetOpen() {
        return bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }

    private void initializeToolbar() {
        toolbar.setTitleTextColor(colorWhite);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}