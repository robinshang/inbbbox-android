package co.netguru.android.inbbbox.feature.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnPageChange;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.analytics.AnalyticsDrawerListener;
import co.netguru.android.inbbbox.common.analytics.AnalyticsEventLogger;
import co.netguru.android.inbbbox.common.utils.AnimationUtil;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import co.netguru.android.inbbbox.feature.main.adapter.MainActivityPagerAdapter;
import co.netguru.android.inbbbox.feature.main.adapter.TabItemType;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpActivity;
import co.netguru.android.inbbbox.feature.shared.view.NonSwipeableViewPager;
import co.netguru.android.inbbbox.feature.shot.ShotsFragment;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsFragment;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import de.hdodenhof.circleimageview.CircleImageView;

import static butterknife.ButterKnife.findById;

public class MainActivity
        extends BaseMvpActivity<MainViewContract.View, MainViewContract.Presenter>
        implements MainViewContract.View,
        ShotsFragment.ShotActionListener,
        TimePickerDialogFragment.OnTimePickedListener {

    public static final int REQUEST_REFRESH_FOLLOWER_LIST = 101;
    public static final int REQUEST_RESTART = 202;

    private static final int REQUEST_DEFAULT = 0;
    private static final String REQUEST_EXTRA = "requestExtra";
    private static final String TOGGLE_BUTTON_STATE = "toggleButtonState";

    private static final String EMPTY_STRING = "";

    public static final int TAB_SHOTS = 0;
    public static final int TAB_LIKES = 1;
    public static final int TAB_BUCKETS = 2;
    public static final int TAB_FOLLOWING = 3;

    @BindColor(R.color.accent)
    int highlightColor;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.main_view_pager)
    NonSwipeableViewPager viewPager;
    @BindView(R.id.activity_main_navigation_view)
    NavigationView navigationView;
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout drawerLayout;

    @BindDrawable(R.drawable.toolbar_center_background)
    Drawable toolbarCenterBackground;
    @BindDrawable(R.drawable.toolbar_start_background)
    Drawable toolbarStartBackground;

    @Inject
    AnalyticsEventLogger analyticsEventLogger;
    @Inject
    AnalyticsDrawerListener analyticsDrawerListener;

    private MainActivityComponent component;
    private TextView drawerUserName;
    private CircleImageView drawerUserPhoto;
    private TextView drawerReminderTime;
    private Switch notificationSwitch;
    private Switch followingSwitch;
    private Switch newSwitch;
    private Switch popularSwitch;
    private Switch debutsSwitch;
    private Switch shotDetailsSwitch;
    private Switch nightModeSwitch;
    private ToggleButton drawerToggleButton;
    private MainActivityPagerAdapter pagerAdapter;
    private View drawerCreateAccountButton;
    private int currentTabIndex = Constants.UNDEFINED;

    public static void startActivity(Context context) {
        final Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void startActivityWithRequest(Context context, int requestCode) {
        final Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(REQUEST_EXTRA, requestCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initComponent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeToolbar();
        initializeDrawer();
        getPresenter().prepareUserData();
        navigationView.setSaveEnabled(false);
        analyticsEventLogger.logEventScreenShots();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(TOGGLE_BUTTON_STATE, drawerToggleButton.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreToggleButtonState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectTab(tabLayout.getTabAt(currentTabIndex));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        switch (intent.getIntExtra(REQUEST_EXTRA, REQUEST_DEFAULT)) {
            case REQUEST_REFRESH_FOLLOWER_LIST:
                pagerAdapter.refreshFragment(TabItemType.FOLLOWERS);
                selectTab(tabLayout.getTabAt(TabItemType.FOLLOWERS.getPosition()));
                break;
            case REQUEST_RESTART:
                finish();
                startActivity(intent);
                break;
            default:
                throw new IllegalStateException("Intent should contains REQUEST_EXTRA");
        }
    }

    @Override
    public void showMessageOnServerError(String errorText) {
        showTextOnSnackbar(errorText);
    }

    @NonNull
    @Override
    public MainViewContract.Presenter createPresenter() {
        return component.getMainActivityPresenter();
    }

    @Override
    public void showLogoutMenu() {
        changeMenuGroupsVisibility(false, true);
    }

    @Override
    public void showMainMenu() {
        changeMenuGroupsVisibility(true, false);
    }

    @Override
    public void showLoginActivity() {
        LoginActivity.startActivity(this);
        finish();
    }

    @Override
    public void showUserName(String username) {
        drawerUserName.setText(username);
    }

    @Override
    public void showUserPhoto(String url) {
        Glide.with(MainActivity.this)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(drawerUserPhoto);
    }

    @Override
    public void showTimePickDialog(int startHour, int startMinute) {
        TimePickerDialogFragment.newInstance(startHour, startMinute)
                .show(getSupportFragmentManager(), TimePickerDialogFragment.TAG);
    }

    @Override
    public void timePicked(int hour, int minute) {
        getPresenter().onTimePicked(hour, minute);
    }

    @Override
    public void showNotificationTime(String time) {
        drawerReminderTime.setText(time);
    }

    @Override
    public void changeNotificationStatus(boolean status) {
        notificationSwitch.setChecked(status);
    }

    @Override
    public void changeFollowingStatus(boolean status) {
        followingSwitch.setChecked(status);
    }

    @Override
    public void changeNewStatus(boolean status) {
        newSwitch.setChecked(status);
    }

    @Override
    public void changePopularStatus(boolean status) {
        popularSwitch.setChecked(status);
    }

    @Override
    public void changeDebutsStatus(boolean status) {
        debutsSwitch.setChecked(status);
    }

    @Override
    public void changeCustomizationStatus(boolean isDetails) {
        shotDetailsSwitch.setChecked(isDetails);
        getPresenter().onShotDetailsVisibilityChange(isDetails);
    }

    @Override
    public void changeNightMode(boolean isNightMode) {
        AppCompatDelegate.setDefaultNightMode(isNightMode
                ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        recreate();
    }

    @Override
    public void setNightModeStatus(boolean isNightMode) {
        nightModeSwitch.setChecked(isNightMode);
    }

    @Override
    public void setSettingsListeners() {
        followingSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                getPresenter().followingStatusChanged(isChecked));
        newSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                getPresenter().newStatusChanged(isChecked));
        popularSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                getPresenter().popularStatusChanged(isChecked));
        debutsSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                getPresenter().debutsStatusChanged(isChecked));
        shotDetailsSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                getPresenter().customizationStatusChanged(isChecked));
        nightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                getPresenter().nightModeChanged(isChecked));
    }

    @Override
    public void showMessage(@StringRes int message) {
        showTextOnSnackbar(getResources().getString(message));
    }

    @Override
    public void showShotDetails(Shot shot, List<Shot> allShots, ShotDetailsRequest
            detailsRequest) {
        showBottomSheet(ShotDetailsFragment.newInstance(shot, allShots, detailsRequest), ShotDetailsFragment.TAG);
    }

    @Override
    public void shotLikeStatusChanged() {
        pagerAdapter.refreshFragment(TabItemType.LIKES);
    }

    @Override
    public void refreshShotsView() {
        pagerAdapter.refreshFragment(TabItemType.SHOTS);
    }

    @Override
    public void openSignUpPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void showCreateAccountButton() {
        drawerCreateAccountButton.setVisibility(View.VISIBLE);
    }

    private void initComponent() {
        component = App.getAppComponent(this)
                .plusMainActivityComponent();
        component.inject(this);
    }

    private void restoreToggleButtonState(Bundle savedInstanceState) {
        final boolean toggleButtonState = savedInstanceState.getBoolean(TOGGLE_BUTTON_STATE, false);
        drawerToggleButton.setChecked(toggleButtonState);
        getPresenter().toggleButtonChanged(toggleButtonState);
    }

    @Override
    public void initializePager(boolean isOnboardingPassed) {
        pagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager(), isOnboardingPassed);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (final TabItemType item : TabItemType.values()) {
            final TabLayout.Tab tab = tabLayout.getTabAt(item.getPosition());
            if (tab != null) {
                tab.setIcon(item.getIcon());
            }
            selectInitialTabSelection(tab, item.getPosition());
        }
        tabLayout.addOnTabSelectedListener(createTabListener());
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
        if (currentTabIndex != Constants.UNDEFINED) {
            unselectedPreviousTabs(currentTabIndex);
        }

        final Drawable icon = tab.getIcon();
        if (icon != null) {
            icon.setColorFilter(highlightColor, PorterDuff.Mode.SRC_IN);
        }
        tab.setText(getString(TabItemType.getTabItemForPosition(currentTabIndex).getTitle()));
        setupToolbarForCurrentTab(currentTabIndex);
    }

    private void unselectedPreviousTabs(int currentTabIndex) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (i != currentTabIndex) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                clearTabsIconHighlight(tab);
                tab.setText(EMPTY_STRING);
            }
        }
    }

    private void clearTabsIconHighlight(TabLayout.Tab tab) {
        final Drawable icon = tab.getIcon();
        if (icon != null) {
            icon.clearColorFilter();
        }
    }

    private void setupToolbarForCurrentTab(int position) {
        toolbar.setBackground(position == TabItemType.SHOTS.getPosition()
                ? toolbarCenterBackground : toolbarStartBackground);
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }
    }

    private void initializeDrawer() {
        changeMenuGroupsVisibility(true, false);
        final View header = navigationView.getHeaderView(0);
        drawerToggleButton = findById(header, R.id.drawer_header_toggle);
        drawerUserName = findById(header, R.id.drawer_user_name);
        drawerUserPhoto = findById(header, R.id.drawer_user_photo);
        drawerCreateAccountButton = findById(header, R.id.create_account_textView);
        drawerReminderTime = findById(navigationView.getMenu()
                .findItem(R.id.drawer_item_reminder).getActionView(), R.id.drawer_item_time);

        drawerToggleButton.setOnCheckedChangeListener(
                (buttonView, isChecked) -> getPresenter().toggleButtonChanged(isChecked));
        drawerCreateAccountButton.setOnClickListener(view -> onCreateAccountClick());

        navigationView.setNavigationItemSelectedListener(this::onNavigationNItemSelected);

        ActionBarDrawerToggle actionBarDrawerToggle = createDrawerToggle();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        initializeDrawerReminder();
        drawerLayout.addDrawerListener(analyticsDrawerListener);
    }

    private void onCreateAccountClick() {
        getPresenter().onCreateAccountClick();
        analyticsEventLogger.logEventCreateAccountAsGuest();
    }

    private boolean onNavigationNItemSelected(MenuItem item) {
        int i = item.getItemId();
        boolean result = false;
        if (i == R.id.drawer_item_logout) {
            getPresenter().performLogout();
        }
        return result;
    }

    private ActionBarDrawerToggle createDrawerToggle() {
        return new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);
    }

    private void initializeDrawerReminder() {
        drawerReminderTime.setOnClickListener(v -> getPresenter().timeViewClicked());
        drawerReminderTime.setEnabled(false);
        initializeDrawerSwitches();
    }

    private void initializeDrawerSwitches() {
        notificationSwitch = findDrawerSwitch(R.id.drawer_item_enable_reminder);
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            drawerReminderTime.setEnabled(isChecked);
            getPresenter().notificationStatusChanged(isChecked);
        });
        followingSwitch = findDrawerSwitch(R.id.drawer_item_following);
        newSwitch = findDrawerSwitch(R.id.drawer_item_new);
        popularSwitch = findDrawerSwitch(R.id.drawer_item_popular);
        debutsSwitch = findDrawerSwitch(R.id.drawer_item_debuts);
        shotDetailsSwitch = findDrawerSwitch(R.id.drawer_item_shot_details);
        nightModeSwitch = findDrawerSwitch(R.id.drawer_item_night_mode);
    }

    private Switch findDrawerSwitch(@IdRes int itemId) {
        return findById(navigationView.getMenu()
                .findItem(itemId).getActionView(), R.id.drawer_item_switch);
    }

    private void changeMenuGroupsVisibility(boolean isMainMenuVisible, boolean isLogoutMenuVisible) {
        navigationView.getMenu().setGroupVisible(R.id.group_all, isMainMenuVisible);
        navigationView.getMenu().setGroupVisible(R.id.group_logout, isLogoutMenuVisible);
    }

    @OnPageChange(R.id.main_view_pager)
    void onPageSelected(int position) {
        switch (TabItemType.getTabItemForPosition(position)) {
            case SHOTS:
                analyticsEventLogger.logEventScreenShots();
                break;
            case LIKES:
                analyticsEventLogger.logEventScreenLikes();
                break;
            case BUCKETS:
                analyticsEventLogger.logEventScreenBuckets();
                break;
            case FOLLOWERS:
                analyticsEventLogger.logEventScreenFollowing();
                break;
            default:
        }
    }

    public void shakeTabIcon(int position) {
        View tabView = ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(position);
        if (tabView != null)
            AnimationUtil.animateShake(tabView);
    }
}
