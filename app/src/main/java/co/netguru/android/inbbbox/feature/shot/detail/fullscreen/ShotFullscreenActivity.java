package co.netguru.android.inbbbox.feature.shot.detail.fullscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseActivity;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;

public class ShotFullscreenActivity extends BaseActivity {

    public static final String KEY_SHOT = "key:shot";
    public static final String KEY_ALL_SHOTS = "key:all_shot";
    public static final String KEY_DETAILS_REQUEST = "key:details_request";

    public static void startActivity(Context context, Shot shot,
                                     List<Shot> allShots, ShotDetailsRequest detailsRequest) {
        Intent intent = new Intent(context, ShotFullscreenActivity.class);
        intent.putExtra(KEY_SHOT, shot);
        intent.putExtra(KEY_DETAILS_REQUEST, detailsRequest);

        if (allShots instanceof ArrayList) {
            intent.putParcelableArrayListExtra(KEY_ALL_SHOTS, (ArrayList<Shot>) allShots);
        } else {
            intent.putParcelableArrayListExtra(KEY_ALL_SHOTS, new ArrayList<Shot>(allShots));
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_fullscreen);

        if (savedInstanceState == null) {
            initializeShotFullscreenFragment();
        }
    }

    private void initializeShotFullscreenFragment() {
        Shot shot = getIntent().getParcelableExtra(KEY_SHOT);
        List<Shot> allShots = getIntent().getParcelableArrayListExtra(KEY_ALL_SHOTS);
        ShotDetailsRequest detailsRequest = getIntent().getParcelableExtra(KEY_DETAILS_REQUEST);

        replaceFragment(R.id.shot_fullscreen_container,
                ShotFullscreenFragment.newInstance(shot, allShots, detailsRequest), ShotFullscreenFragment.TAG)
                .commit();
    }

}
