package co.netguru.android.inbbbox.feature.followers.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseActivity;
import co.netguru.android.inbbbox.feature.details.ShotDetailsFragment;
import co.netguru.android.inbbbox.feature.main.MainActivity;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;

public class FollowerDetailsActivity extends BaseActivity
        implements FollowerDetailsFragment.OnUnFollowCompletedListener {

    private static final String FOLLOWER_KEY = "follower_key";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindColor(R.color.white)
    int colorWhite;

    @BindView(R.id.fragment_container)
    View bottomSheetView;

    private BottomSheetBehavior<View> bottomSheetBehavior;

    public static void startActivity(Context context, Follower follower) {
        final Intent intent = new Intent(context, FollowerDetailsActivity.class);
        intent.putExtra(FOLLOWER_KEY, follower);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_details);
        initializeToolbar();
        initializeBottomSheet();
        if (savedInstanceState == null) {
            replaceFragment(R.id.follower_details_fragment_container,
                    FollowerDetailsFragment.newInstance(getIntent().getParcelableExtra(FOLLOWER_KEY)), FollowerDetailsFragment.TAG).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.follower_details_menu, menu);
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
    public void onBackPressed() {
        if (isBottomSheetOpen()) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void unFollowCompleted() {
        finish();
        MainActivity.startActivityWithRequest(this, MainActivity.REQUEST_REFRESH_FOLLOWER_LIST);
    }

    private void initializeBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
    }

    @Override
    public void showShotDetails(Shot shot) {
        Fragment fragment = ShotDetailsFragment.newInstance(shot);
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
