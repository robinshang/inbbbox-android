package co.netguru.android.inbbbox.feature.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseActivity;
import co.netguru.android.inbbbox.feature.main.adapter.MainActivityPagerAdapter;
import co.netguru.android.inbbbox.view.NonSwipeableViewPager;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.main_view_pager)
    NonSwipeableViewPager viewPager;

    MainActivityPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializePager();
    }

    private void initializePager() {
        pagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setHorizontalScrollBarEnabled(false);
        tabLayout.setupWithViewPager(viewPager);
        for (final MenuItem item : MenuItem.values()) {
            tabLayout.getTabAt(item.getPosition()).setIcon(item.getIcon());
        }
    }
}
