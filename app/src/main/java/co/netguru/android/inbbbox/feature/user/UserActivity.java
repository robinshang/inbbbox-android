package co.netguru.android.inbbbox.feature.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.analytics.AnalyticsEventLogger;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.main.MainActivity;
import co.netguru.android.inbbbox.feature.shared.UserDetailsTabItemType;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpActivity;
import co.netguru.android.inbbbox.feature.shared.view.NonSwipeableViewPager;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsFragment;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsType;
import co.netguru.android.inbbbox.feature.user.followdialog.UnFollowUserDialogFragment;
import co.netguru.android.inbbbox.feature.user.info.team.ShotActionListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity
        extends BaseMvpActivity<UserActivityContract.View, UserActivityContract.Presenter>
        implements UserActivityContract.View, ShotActionListener,
        UnFollowUserDialogFragment.OnUnFollowClickedListener {

    private static final String USER_KEY = "user_key";
    private static final String TEXT_PLAIN = "text/plain";

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
    @Inject
    AnalyticsEventLogger analyticsEventLogger;
    private UserActivityComponent component;
    private boolean shouldRefreshFollowers;
    private MenuItem itemFollow;
    private MenuItem itemUnfollow;
    private User user;

    public static void startActivity(Context context, User user) {
        final Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(USER_KEY, user);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initComponent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        user = getIntent().getParcelableExtra(USER_KEY);

        initializePager();
        initializeToolbar();
        setupImage();
        shouldRefreshFollowers = false;
        getPresenter().checkFollowingStatus(user);
        logScreenEvent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.follower_details_menu, menu);
        itemFollow = menu.findItem(R.id.action_follow);
        itemUnfollow = menu.findItem(R.id.action_unfollow);
        itemFollow.setVisible(false);
        itemUnfollow.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_follow:
                changeFollowingStatus(true);
                return true;
            case R.id.action_unfollow:
                changeFollowingStatus(false);
                return true;
            case R.id.action_share:
                getPresenter().shareUser(user);
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

    private void initializePager() {
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

    private void initializeToolbar() {
        collapsingToolbarLayout.setTitleEnabled(false);
        toolbar.setTitleTextColor(colorWhite);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(user.name());
        }
    }

    private void setupImage() {
        Glide.with(this)
                .load(user.avatarUrl())
                .fitCenter()
                .error(R.drawable.ic_ball)
                .into(userImageView);
    }

    @NonNull
    @Override
    public UserActivityContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @Override
    public void showFollowingAction(boolean following) {
        itemFollow.setVisible(following);
        itemUnfollow.setVisible(!following);
    }

    private void initComponent() {
        component = App.getUserComponent(this).plusUserActivityComponent();
        component.inject(this);
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        Snackbar.make(userImageView, errorText, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showShare(String toShare) {
        final Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType(TEXT_PLAIN);
        sendIntent.putExtra(Intent.EXTRA_TEXT, toShare);
        startActivity(sendIntent);
    }

    private void logScreenEvent() {
        if (User.TYPE_TEAM.equals(user.type()))
            analyticsEventLogger.logEventScreenTeamDetails();
        else
            analyticsEventLogger.logEventScreenUserDetails();
    }

    private void changeFollowingStatus(boolean follow) {
        shouldRefreshFollowers = true;
        analyticsEventLogger.logEventAppbarFollow(follow);
        getPresenter().changeFollowingStatus(user, follow);
    }

    @Override
    public void showUnfollowDialog(String username) {
        UnFollowUserDialogFragment.newInstance(username)
                .show(getSupportFragmentManager(), UnFollowUserDialogFragment.TAG);
    }

    @Override
    public void onUnFollowClicked() {
        getPresenter().stopFollowing(user);
    }
}
