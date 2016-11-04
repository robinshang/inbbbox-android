package co.netguru.android.inbbbox.feature.shots;

import co.netguru.android.inbbbox.models.ShotEntity;
import co.netguru.android.inbbbox.models.ui.Shot;

public class ShotsMapper {

    public Shot getShot(ShotEntity entity) {
        return Shot.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .hdpiImageUrl(entity.getImage().hiDpiUrl())
                .normalImageUrl(entity.getImage().normalUrl())
                .thumbnailUrl(entity.getImage().teaserUrl())
                .isLiked(false)
                .build();
    }
}
