package co.netguru.android.inbbbox.feature.main;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseActivity;
import co.netguru.android.inbbbox.feature.main.adapter.MainActivityPagerAdapter;
import co.netguru.android.inbbbox.model.ui.TabItemType;
import co.netguru.android.inbbbox.view.NonSwipeableViewPager;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.main_view_pager)
    NonSwipeableViewPager viewPager;
    @BindView(R.id.activity_main_navigation_view)
    NavigationView navigationView;

    private MainActivityPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializePager();
        initializeToolbar();
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
                // TODO: 13.10.2016  Add action on menu item click
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
}
