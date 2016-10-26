package co.netguru.android.inbbbox.feature.shots;

import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.data.ui.Shot;

public class ShotsMapper {

    public Shot getShot(ShotEntity entity) {
        Shot shot = new Shot();

        shot.setId(entity.getId());
        shot.setTitle(entity.getTitle());
        shot.setDescription(entity.getDescription());
        shot.setHdpiImage(entity.getImages().getHidpi());
        shot.setNormalImage(entity.getImages().getNormal());
        shot.setThumbnail(entity.getImages().getTeaser());
        return shot;
    }
}
