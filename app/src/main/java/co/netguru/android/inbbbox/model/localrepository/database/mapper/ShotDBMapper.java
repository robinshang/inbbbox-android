package co.netguru.android.inbbbox.model.localrepository.database.mapper;

import co.netguru.android.inbbbox.model.localrepository.database.ShotDB;
import co.netguru.android.inbbbox.model.ui.Shot;

public class ShotDBMapper {

    private static final int DEFAULT_ID = -1;

    private ShotDBMapper() {
        throw new AssertionError();
    }

    public static ShotDB fromShot(Shot shot) {
        final Long authorId = shot.author() != null ? shot.author().id() : DEFAULT_ID;
        final Long teamId = shot.team() != null ? shot.team().id() : DEFAULT_ID;
        return new ShotDB(shot.id(), shot.title(), shot.creationDate(), shot.projectUrl(),
                shot.likesCount(), shot.bucketCount(), shot.commentsCount(), shot.description(),
                shot.isGif(), shot.hiDpiImageUrl(), shot.normalImageUrl(), shot.thumbnailUrl(),
                shot.isBucketed(), shot.isLiked(), authorId, teamId);
    }
}
