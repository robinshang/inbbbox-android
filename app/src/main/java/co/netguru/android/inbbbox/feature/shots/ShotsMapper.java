package co.netguru.android.inbbbox.feature.shots;

import java.util.List;

import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.data.ui.Shot;

public class ShotsMapper {

    public Shot getShot(ShotEntity entity, List<Integer> likedShotsId) {
        return Shot.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .hdpiImageUrl(entity.getImage().hiDpiUrl())
                .normalImageUrl(entity.getImage().normalUrl())
                .thumbnailUrl(entity.getImage().teaserUrl())
                .likeStatus(likedShotsId.contains(entity.getId()) ? Shot.LIKED : Shot.UNLIKED)
                .build();
    }
}
