package co.netguru.android.inbbbox.feature.shots;

import java.util.List;

import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.data.viewmodels.Shot;

public class ShotsMapper {

    public Shot getShot(ShotEntity entity) {
        Shot shot = new Shot();

        shot.setTitle(entity.getTitle());
        return shot;
    }
}
