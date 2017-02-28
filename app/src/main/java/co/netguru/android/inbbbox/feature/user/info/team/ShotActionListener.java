package co.netguru.android.inbbbox.feature.user.info.team;

import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

public interface ShotActionListener {
    void showShotDetails(Shot shot, List<Shot> allShots, long userId);
}
