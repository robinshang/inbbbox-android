package co.netguru.android.inbbbox.feature.shot.detail.fullscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseActivity;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;

public class ShotFullscreenActivity extends BaseActivity {

    public static final String KEY_PREVIEW_SHOT_INDEX = "key:preview_shot_index";
    public static final String KEY_ALL_SHOTS = "key:all_shot";
    public static final String KEY_DETAILS_REQUEST = "key:details_request";

    public static void startActivitySingleShot(Context context, Shot shot, ShotDetailsRequest detailsRequest) {
        startActivity(context, Arrays.asList(shot), 0, detailsRequest);
    }

    public static void startActivity(Context context, List<Shot> allShots, int previewShotIndex,
                                     ShotDetailsRequest detailsRequest) {
        Intent intent = new Intent(context, ShotFullscreenActivity.class);
        intent.putExtra(KEY_PREVIEW_SHOT_INDEX, previewShotIndex);
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
        int previewShotIndex = getIntent().getIntExtra(KEY_PREVIEW_SHOT_INDEX, 0);
        List<Shot> allShots = getIntent().getParcelableArrayListExtra(KEY_ALL_SHOTS);
        ShotDetailsRequest detailsRequest = getIntent().getParcelableExtra(KEY_DETAILS_REQUEST);

        replaceFragment(R.id.shot_fullscreen_container,
                ShotFullscreenFragment.newInstance(allShots, previewShotIndex, detailsRequest), ShotFullscreenFragment.TAG)
                .commit();
    }

}
