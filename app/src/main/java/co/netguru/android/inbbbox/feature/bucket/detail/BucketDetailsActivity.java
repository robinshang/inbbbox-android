package co.netguru.android.inbbbox.feature.bucket.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.bucket.model.ui.BucketWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.ShotClickListener;
import co.netguru.android.inbbbox.feature.shared.base.BaseActivity;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsFragment;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsType;


public class BucketDetailsActivity extends BaseActivity
        implements ShotClickListener {

    private static final String BUCKET_WITH_SHOTS_KEY = "bucket_with_shots_key";
    private static final String SHOTS_PER_PAGE_KEY = "shots_per_page_key";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindColor(R.color.white)
    int colorWhite;

    public static void startActivityForDeleteResult(Fragment fragment, Context context, int requestCode,
                                                    BucketWithShots bucketWithShots, int perPage) {
        final Intent intent = new Intent(context, BucketDetailsActivity.class);
        intent.putExtra(BUCKET_WITH_SHOTS_KEY, bucketWithShots);
        intent.putExtra(SHOTS_PER_PAGE_KEY, perPage);

        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_details);
        initializeToolbar();
        if (savedInstanceState == null) {
            initializeBucketDetailsFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bucket_details_menu, menu);
        return true;
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
    public void onShotClick(Shot shot) {
        BucketDetailsFragment bucketDetailsFragment =
                (BucketDetailsFragment) getSupportFragmentManager().findFragmentByTag(BucketDetailsFragment.TAG);
        BucketWithShots bucketWithShots = getIntent().getParcelableExtra(BUCKET_WITH_SHOTS_KEY);

        ShotDetailsRequest request = ShotDetailsRequest.builder()
                .detailsType(ShotDetailsType.BUCKET)
                .id(bucketWithShots.bucket().id())
                .build();

        final Fragment fragment = ShotDetailsFragment.newInstance(shot,
                bucketDetailsFragment.getData(), request);
        showBottomSheet(fragment, ShotDetailsFragment.TAG);
    }

    private void initializeBucketDetailsFragment() {
        BucketWithShots bucketWithShots = getIntent().getParcelableExtra(BUCKET_WITH_SHOTS_KEY);
        int perPage = getIntent().getIntExtra(SHOTS_PER_PAGE_KEY, Constants.UNDEFINED);
        if (bucketWithShots != null && perPage != Constants.UNDEFINED) {
            replaceFragment(R.id.bucket_details_fragment_container,
                    BucketDetailsFragment.newInstance(bucketWithShots, perPage), BucketDetailsFragment.TAG).commit();
        } else {
            throw new IllegalArgumentException("Error shots with buckets or per page count is empty");
        }
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