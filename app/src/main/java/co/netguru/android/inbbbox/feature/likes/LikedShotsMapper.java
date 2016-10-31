package co.netguru.android.inbbbox.feature.likes;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.data.models.LikedShotEntity;
import co.netguru.android.inbbbox.data.ui.LikedShot;

@FragmentScope
final class LikedShotsMapper {

    @Inject
    LikedShotsMapper() {

    }

    public LikedShot toLikedShot(LikedShotEntity entity) {
        return new LikedShot(entity.shot().getId(),
                entity.shot().getImages().normalUrl());
    }
}
