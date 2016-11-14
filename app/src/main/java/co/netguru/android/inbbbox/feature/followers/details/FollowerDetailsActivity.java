package co.netguru.android.inbbbox.feature.followers.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseActivity;
import co.netguru.android.inbbbox.model.ui.Follower;


public class FollowerDetailsActivity extends BaseActivity {

    private static final String FOLLOWER_KEY = "follower_key";

    public static void startActivity(Context context, Follower follower) {
        final Intent intent = new Intent(context, FollowerDetailsActivity.class);
        intent.putExtra(FOLLOWER_KEY, follower);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_details);
        if (savedInstanceState == null && getIntent().hasExtra(FOLLOWER_KEY)) {
            replaceFragment(R.id.follower_details_fragment_container,
                    FollowerDetailsFragment.newInstance(getIntent().getParcelableExtra(FOLLOWER_KEY)), FollowerDetailsFragment.TAG).commit();
        }
    }
    // TODO: 14.11.2016 Init actionbar
}
