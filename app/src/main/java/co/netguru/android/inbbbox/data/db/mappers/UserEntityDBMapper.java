package co.netguru.android.inbbbox.data.db.mappers;

import co.netguru.android.inbbbox.data.db.UserEntityDB;
import co.netguru.android.inbbbox.data.dribbbleuser.user.model.api.UserEntity;

public class UserEntityDBMapper {

    private UserEntityDBMapper() {
        throw new AssertionError();
    }

    public static UserEntityDB fromUserEntity(UserEntity user) {
        return new UserEntityDB(user.id(), user.name(), user.username(), user.htmlUrl(), user.avatarUrl(),
                user.bio(), user.location(), user.links().web(), user.links().twitter(), user.bucketsCount(),
                user.commentsReceivedCount(), user.followersCount(), user.followingsCount(), user.likesCount(),
                user.likesReceivedCount(), user.projectsCount(), user.reboundsReceivedCount(), user.shotsCount(),
                user.teamsCount(), user.canUploadShot(), user.type(), user.pro(), user.bucketsUrl(), user.followersUrl(),
                user.followingUrl(), user.likesUrl(), user.shotsUrl(), user.teamsUrl(), user.createdAt(), user.updatedAt());
    }
}
