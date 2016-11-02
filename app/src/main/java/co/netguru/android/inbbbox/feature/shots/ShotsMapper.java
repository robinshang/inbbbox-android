package co.netguru.android.inbbbox.feature.shots;

import java.util.List;

import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.data.ui.Shot;

public class ShotsMapper {

    // TODO: 02.11.2016 LikedShotsList should be changed
    public Shot getShot(ShotEntity entity, List<Integer> likedShotsId) {
        return Shot.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .hdpiImageUrl(entity.getImage().hiDpiUrl())
                .normalImageUrl(entity.getImage().normalUrl())
                .thumbnailUrl(entity.getImage().teaserUrl())
                .isLiked(likedShotsId.contains(entity.getId()))
                .build();
    }
}
