package co.netguru.android.inbbbox.feature.team;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.feature.shared.UserDetailsActivityPagerAdapter;
import co.netguru.android.inbbbox.feature.shared.UserDetailsTabItemType;
import co.netguru.android.inbbbox.feature.shared.base.BaseActivity;
import co.netguru.android.inbbbox.feature.shared.view.NonSwipeableViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeamDetailsActivity extends BaseActivity {

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
        setupImage();
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

            UserWithShots user = getIntent().getParcelableExtra(USER_KEY);
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