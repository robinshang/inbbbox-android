package co.netguru.android.inbbbox.feature.likes;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.data.ui.LikedShot;
import co.netguru.android.inbbbox.di.scope.FragmentScope;

@FragmentScope
final class LikedShotsMapper {

    @Inject
    LikedShotsMapper() {

    }

    public LikedShot toLikedShot(ShotEntity entity) {
        return new LikedShot(entity.getId(), entity.getImages().getNormal());
    }
}
