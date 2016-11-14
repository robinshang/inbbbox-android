package co.netguru.android.inbbbox.feature.followers.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseActivity;


public class FollowerDetailsActivity extends BaseActivity {

    public static void startActivity(Context context) {
        final Intent intent = new Intent(context, FollowerDetailsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_follower_details);
        if (savedInstanceState == null) {
            replaceFragment(R.id.follower_details_fragment_container, FollowerDetailsFragment.newInstance(), FollowerDetailsFragment.TAG);
        }
    }
    // TODO: 14.11.2016 Init actionbar
}
