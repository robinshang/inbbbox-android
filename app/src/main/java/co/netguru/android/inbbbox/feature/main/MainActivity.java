package co.netguru.android.inbbbox.feature.main;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.MainActivityComponent;
import co.netguru.android.inbbbox.enumeration.TabItemType;
import co.netguru.android.inbbbox.feature.common.BaseMvpActivity;
import co.netguru.android.inbbbox.feature.likes.LikesFragment;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import co.netguru.android.inbbbox.feature.main.adapter.MainActivityPagerAdapter;
import co.netguru.android.inbbbox.feature.shots.ShotsFragment;
import co.netguru.android.inbbbox.view.NonSwipeableViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

import static butterknife.ButterKnife.findById;

public class MainActivity extends BaseMvpActivity<MainViewContract.View, MainViewContract.Presenter>
        implements MainViewContract.View, ShotsFragment.ShotLikeStatusListener {

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

    @BindString(R.string.empty_string)
    String emptyString;

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
    private MainActivityPagerAdapter pagerAdapter;

    public static void startActivity(Context context) {
        final Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initComponent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializePager();
        initializeToolbar();
        initializeDrawer();
        getPresenter().prepareUserData();
    }

    private void initComponent() {
        component = App.getAppComponent(this)
                .plusMainActivityComponent();
        component.inject(this);
    }

    @NonNull
    @Override
    public MainViewContract.Presenter createPresenter() {
        return component.getMainActivityPresenter();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    private void initializePager() {
        pagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (final TabItemType item : TabItemType.values()) {
            final TabLayout.Tab tab = tabLayout.getTabAt(item.getPosition());
            if (tab != null) {
                tab.setIcon(item.getIcon());
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab(tab);
                toolbar.setBackground(tab.getPosition() == TabItemType.SHOTS.getPosition()
                        ? toolbarCenterBackground : toolbarStartBackground);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                final Drawable icon = tab.getIcon();
                if (icon != null) {
                    icon.clearColorFilter();
                }
                tab.setText(emptyString);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                selectTab(tab);
            }
        });
        reselectFirstTab();
    }

    private void selectTab(TabLayout.Tab tab) {
        final Drawable icon = tab.getIcon();
        if (icon != null) {
            icon.setColorFilter(getResources().getColor(R.color.pink), PorterDuff.Mode.SRC_IN);
        }
        tab.setText(getString(TabItemType.getTabItemForPosition(tab.getPosition()).getTitle()));
    }

    private void reselectFirstTab() {
        final TabLayout.Tab firstTab = tabLayout.getTabAt(0);
        if (firstTab != null) {
            firstTab.select();
        }
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
        final ToggleButton drawerToggle = findById(header, R.id.drawer_header_toggle);
        drawerUserName = findById(header, R.id.drawer_user_name);
        drawerUserPhoto = findById(header, R.id.drawer_user_photo);
        drawerReminderTime = findById(navigationView.getMenu()
                .findItem(R.id.drawer_item_reminder).getActionView(), R.id.drawer_item_time);

        drawerToggle.setOnCheckedChangeListener(
                (buttonView, isChecked) -> getPresenter().toggleButtonClicked(isChecked));

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.drawer_item_logout:
                    getPresenter().performLogout();
                    break;
            }
            return true;
        });

        ActionBarDrawerToggle mDrawerToggle = getDrawerToggle();
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();

        initializeDrawerReminder();
    }

    private ActionBarDrawerToggle getDrawerToggle() {
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
        followingSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> getPresenter().followingStatusChanged(isChecked)));
        newSwitch = findDrawerSwitch(R.id.drawer_item_new);
        newSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> getPresenter().newStatusChanged(isChecked)));
        popularSwitch = findDrawerSwitch(R.id.drawer_item_popular);
        popularSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> getPresenter().popularStatusChanged(isChecked)));
        debutsSwitch = findDrawerSwitch(R.id.drawer_item_debuts);
        debutsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> getPresenter().debutsStatusChanged(isChecked));
        shotDetailsSwitch = findDrawerSwitch(R.id.drawer_item_shot_details);
        shotDetailsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> getPresenter().customizationStatusChanged(isChecked));
    }

    private Switch findDrawerSwitch(@IdRes int itemId) {
        return findById(navigationView.getMenu().findItem(itemId).getActionView(), R.id.drawer_item_switch);
    }

    private void changeMenuGroupsVisibility(boolean isMainMenuVisible, boolean isLogoutMenuVisible) {
        navigationView.getMenu().setGroupVisible(R.id.group_all, isMainMenuVisible);
        navigationView.getMenu().setGroupVisible(R.id.group_logout, isLogoutMenuVisible);
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
    public void showTimePickDialog(int startHour, int startMinute, TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        new TimePickerDialog(this, R.style.TimePickerDialog, onTimeSetListener, startHour, startMinute, false).show();
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
    }

    @Override
    public void showMessage(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void shotLikeStatusChanged() {
        final List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (final Fragment fragment : fragments) {
            if (fragment instanceof LikesFragment) {
                ((LikesFragment) fragment).refreshFragmentData();
                break;
            }
        }
    }
}
