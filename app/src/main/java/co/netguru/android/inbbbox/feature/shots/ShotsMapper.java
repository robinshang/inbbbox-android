package co.netguru.android.inbbbox.feature.shots;

import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.data.ui.Shot;

public class ShotsMapper {

    public Shot getShot(ShotEntity entity) {
        Shot shot = new Shot();

        shot.setTitle(entity.getTitle());
        return shot;
    }
}
