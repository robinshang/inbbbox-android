package co.netguru.android.inbbbox.feature.followers.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseActivity;
import co.netguru.android.inbbbox.model.ui.Follower;


public class FollowerDetailsActivity extends BaseActivity {

    public static void startActivity(Context context, Follower follower) {
        final Intent intent = new Intent(context, FollowerDetailsActivity.class);
        intent.putExtra("test", follower);
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
