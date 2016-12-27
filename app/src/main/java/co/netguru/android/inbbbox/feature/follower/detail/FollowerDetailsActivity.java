package co.netguru.android.inbbbox.feature.follower.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.base.BaseActivity;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsFragment;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsType;
import co.netguru.android.inbbbox.feature.main.MainActivity;
import co.netguru.android.inbbbox.data.follower.model.ui.Follower;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;

public class FollowerDetailsActivity extends BaseActivity
        implements FollowerDetailsFragment.OnFollowedShotActionListener {

    private static final String FOLLOWER_KEY = "follower_key";
    private static final String USER_KEY = "user_key";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindColor(R.color.white)
    int colorWhite;

    public static void startActivity(Context context, Follower follower) {
        final Intent intent = new Intent(context, FollowerDetailsActivity.class);
        intent.putExtra(FOLLOWER_KEY, follower);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, User user) {
        final Intent intent = new Intent(context, FollowerDetailsActivity.class);
        intent.putExtra(USER_KEY, user);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_details);
        initializeToolbar();
        if (savedInstanceState == null) {
            instantiateFragment();
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
    public void unFollowCompleted() {
        MainActivity.startActivityWithRequest(this, MainActivity.REQUEST_REFRESH_FOLLOWER_LIST);
        finish();
    }

    @Override
    public void showShotDetails(Shot shot, List<Shot> allShots, long userId) {
        ShotDetailsRequest detailsRequest = ShotDetailsRequest.builder()
                .detailsType(ShotDetailsType.USER)
                .id(userId)
                .build();
        final Fragment fragment = ShotDetailsFragment.newInstance(shot, allShots, detailsRequest);
        showBottomSheet(fragment, ShotDetailsFragment.TAG);
    }

    private void initializeToolbar() {
        toolbar.setTitleTextColor(colorWhite);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void instantiateFragment() {
        if (getIntent().getParcelableExtra(FOLLOWER_KEY) != null) {
            instantiateFragmentWithFollower();

        } else if (getIntent().getParcelableExtra(USER_KEY) != null) {
            instantiateFragmentWithUser();
        }
    }

    private void instantiateFragmentWithFollower() {
        replaceFragment(R.id.follower_details_fragment_container,
                FollowerDetailsFragment.newInstanceWithFollower(getIntent().getParcelableExtra(FOLLOWER_KEY)),
                FollowerDetailsFragment.TAG).commit();
    }

    private void instantiateFragmentWithUser() {
        replaceFragment(R.id.follower_details_fragment_container,
                FollowerDetailsFragment.newInstanceWithUser(getIntent().getParcelableExtra(USER_KEY)),
                FollowerDetailsFragment.TAG).commit();
    }
}
