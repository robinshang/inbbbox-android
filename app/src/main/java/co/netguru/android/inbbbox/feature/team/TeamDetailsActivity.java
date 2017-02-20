package co.netguru.android.inbbbox.feature.team;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.UserDetailsActivityPagerAdapter;
import co.netguru.android.inbbbox.feature.shared.UserDetailsTabItemType;
import co.netguru.android.inbbbox.feature.shared.base.BaseActivity;
import co.netguru.android.inbbbox.feature.shared.view.NonSwipeableViewPager;
import co.netguru.android.inbbbox.feature.shot.ShotsFragment;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;

public class TeamDetailsActivity extends BaseActivity implements ShotsFragment.ShotActionListener {

    private static final String USER_KEY = "user_key";

    @BindColor(R.color.white)
    int colorWhite;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user_details_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.user_details_view_pager)
    NonSwipeableViewPager viewPager;

    @BindDrawable(R.drawable.toolbar_center_background)
    Drawable toolbarCenterBackground;
    @BindDrawable(R.drawable.toolbar_start_background)
    Drawable toolbarStartBackground;

    @BindColor(R.color.accent)
    int highlightColor;

    private int currentTabIndex = Constants.UNDEFINED;
    private UserDetailsActivityPagerAdapter pagerAdapter;

    public static void startActivity(Context context, UserWithShots user) {
        final Intent intent = new Intent(context, TeamDetailsActivity.class);
        intent.putExtra(USER_KEY, user);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        initializeToolbar();
        initializePager();
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

    public void initializePager() {
        pagerAdapter = new UserDetailsActivityPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (final UserDetailsTabItemType item : UserDetailsTabItemType.values()) {
            final TabLayout.Tab tab = tabLayout.getTabAt(item.getPosition());
            selectInitialTabSelection(tab, item.getPosition());
        }
        tabLayout.addOnTabSelectedListener(createTabListener());
    }

    private void initializeToolbar() {
        toolbar.setTitleTextColor(colorWhite);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            UserWithShots user = getIntent().getParcelableExtra(USER_KEY);
            actionBar.setTitle(user.user().name());
        }
    }

    private void selectInitialTabSelection(TabLayout.Tab tab, int position) {
        if (position == 0) {
            selectTab(tab);
        }
    }

    private TabLayout.OnTabSelectedListener createTabListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab(tab);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //no op

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                selectTab(tab);
            }
        };
    }

    private void selectTab(TabLayout.Tab tab) {
        currentTabIndex = tab.getPosition();

        tab.setText(getString(UserDetailsTabItemType.getTabItemForPosition(tab.getPosition()).getTitle()));
        setupToolbarForCurrentTab(currentTabIndex);
    }

    private void setupToolbarForCurrentTab(int position) {
        toolbar.setBackground(position == UserDetailsTabItemType.SHOTS.getPosition()
                ? toolbarCenterBackground : toolbarStartBackground);
    }

    @Override
    public void shotLikeStatusChanged() {

    }

    @Override
    public void showShotDetails(Shot shot, List<Shot> nearbyShots, ShotDetailsRequest detailsRequest) {

    }
}