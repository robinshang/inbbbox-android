package co.netguru.android.inbbbox.feature.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.feature.shared.base.BaseActivity;

public class TeamDetailsActivity extends BaseActivity {

    private static final String USER_KEY = "user_key";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindColor(R.color.white)
    int colorWhite;

    public static void startActivity(Context context, UserWithShots user) {
        final Intent intent = new Intent(context, TeamDetailsActivity.class);
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

    private void instantiateFragment() {
        replaceFragment(R.id.follower_details_fragment_container,
                TeamDetailsFragment.newInstance(getIntent().getParcelableExtra(USER_KEY)),
                TeamDetailsFragment.TAG).commit();
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

}