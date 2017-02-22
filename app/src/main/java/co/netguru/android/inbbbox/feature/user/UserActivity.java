package co.netguru.android.inbbbox.feature.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.main.MainActivity;
import co.netguru.android.inbbbox.feature.shared.UserDetailsTabItemType;
import co.netguru.android.inbbbox.feature.shared.base.BaseActivity;
import co.netguru.android.inbbbox.feature.shared.view.NonSwipeableViewPager;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsFragment;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsType;
import co.netguru.android.inbbbox.feature.user.shots.UserShotsFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends BaseActivity implements UserShotsFragment.OnFollowedShotActionListener {

    private static final String USER_KEY = "user_key";

    @BindColor(R.color.white)
    int colorWhite;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user_details_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.user_details_view_pager)
    NonSwipeableViewPager viewPager;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.details_user_imageView)
    CircleImageView userImageView;

    private boolean shouldRefreshFollowers;

    public static void startActivity(Context context, UserWithShots user) {
        final Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(USER_KEY, user);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        UserWithShots user = getIntent().getParcelableExtra(USER_KEY);

        initializePager(user);
        initializeToolbar(user);
        setupImage();
        shouldRefreshFollowers = false;
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
    public void showShotDetails(Shot shot, List<Shot> allShots, long userId) {
        ShotDetailsRequest detailsRequest = ShotDetailsRequest.builder()
                .detailsType(ShotDetailsType.USER)
                .id(userId)
                .build();
        final Fragment fragment = ShotDetailsFragment.newInstance(shot, allShots, detailsRequest);
        showBottomSheet(fragment, ShotDetailsFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        if (shouldRefreshFollowers) {
            MainActivity.startActivityWithRequest(this, MainActivity.REQUEST_REFRESH_FOLLOWER_LIST);
            shouldRefreshFollowers = false;
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void initializePager(UserWithShots user) {
        UserPagerAdapter pagerAdapter = new UserPagerAdapter(getSupportFragmentManager(), user);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (final UserDetailsTabItemType item : UserDetailsTabItemType.values()) {
            final TabLayout.Tab tab = tabLayout.getTabAt(item.getPosition());
            if (tab != null) {
                tab.setText(item.getTitle());
            }
        }
    }

    private void initializeToolbar(UserWithShots user) {
        collapsingToolbarLayout.setTitleEnabled(false);
        toolbar.setTitleTextColor(colorWhite);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(user.user().name());
        }
    }

    private void setupImage() {
        UserWithShots user = getIntent().getParcelableExtra(USER_KEY);
        Glide.with(this)
                .load(user.user().avatarUrl())
                .fitCenter()
                .error(R.drawable.ic_ball)
                .into(userImageView);
    }
}
