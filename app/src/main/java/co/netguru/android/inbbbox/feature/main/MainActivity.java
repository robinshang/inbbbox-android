package co.netguru.android.inbbbox.feature.main;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.application.App;
import co.netguru.android.inbbbox.di.component.DaggerMainActivityComponent;
import co.netguru.android.inbbbox.feature.common.BaseMvpActivity;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import co.netguru.android.inbbbox.feature.main.adapter.MainActivityPagerAdapter;
import co.netguru.android.inbbbox.model.ui.TabItemType;
import co.netguru.android.inbbbox.view.NonSwipeableViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseMvpActivity<MainViewContract.View, MainViewContract.Presenter>
        implements MainViewContract.View {

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

    private TextView drawerUserName;
    private CircleImageView drawerUserPhoto;

    private MainActivityPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializePager();
        initializeDrawer();
        initializeToolbar();
    }

    @NonNull
    @Override
    public MainViewContract.Presenter createPresenter() {
        return DaggerMainActivityComponent.builder()
                .applicationComponent(App.getAppComponent(getApplicationContext()))
                .build()
                .getMainActivityPresenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                final Drawable icon = tab.getIcon();
                if (icon != null) {
                    icon.clearColorFilter();
                }
                tab.setText(getString(R.string.empty_string));
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
        }
    }

    private void initializeDrawer() {
        changeMenuGroupsVisibility(true, false);
        final View header = navigationView.getHeaderView(0);
        final ToggleButton drawerToggle = ButterKnife.findById(header, R.id.drawer_header_toggle);
        drawerUserName = ButterKnife.findById(header, R.id.drawer_user_name);
        drawerUserPhoto = ButterKnife.findById(header, R.id.drawer_user_photo);

        drawerToggle.setOnCheckedChangeListener(
                (buttonView, isChecked) -> getPresenter().toggleButtonClicked(isChecked));

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_logout:
                    presenter.performLogout();
                    break;
            }
            return true;
        });
    }

    private void changeMenuGroupsVisibility(boolean isMainMenuVisible, boolean isLogoutMenuVisible) {
        navigationView.getMenu().setGroupVisible(R.id.group_all, isMainMenuVisible);
        navigationView.getMenu().setGroupVisible(R.id.group_logout, isLogoutMenuVisible);
    }

    @Override
    public void showLogoutMenu() {
        changeMenuGroupsVisibility(false, true);
        drawerUserName.setText("Some User");
    }

    @Override
    public void showMainMenu() {
        changeMenuGroupsVisibility(true, false);
        drawerUserName.setText("someemail@gmail.com");
    }

    @Override
    public void showLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
