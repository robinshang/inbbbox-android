package co.netguru.android.inbbbox.feature.followers.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseActivity;
import co.netguru.android.inbbbox.model.ui.Follower;


public class FollowerDetailsActivity extends BaseActivity {

    private static final String FOLLOWER_KEY = "follower_key";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static void startActivity(Context context, Follower follower) {
        final Intent intent = new Intent(context, FollowerDetailsActivity.class);
        intent.putExtra(FOLLOWER_KEY, follower);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_details);
        initializeToolbar();
        if (savedInstanceState == null && getIntent().hasExtra(FOLLOWER_KEY)) {
            replaceFragment(R.id.follower_details_fragment_container,
                    FollowerDetailsFragment.newInstance(getIntent().getParcelableExtra(FOLLOWER_KEY)), FollowerDetailsFragment.TAG).commit();
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
            case R.id.action_follow:
                // TODO: 14.11.2016 Add action on action follow clicked
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeToolbar() {
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
