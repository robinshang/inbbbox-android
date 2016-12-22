package co.netguru.android.inbbbox.model.localrepository.database.mapper;

import co.netguru.android.inbbbox.model.api.FollowerEntity;
import co.netguru.android.inbbbox.model.localrepository.database.FollowerEntityDB;

public class FollowerEntityDBMapper {

    private FollowerEntityDBMapper() {
        throw new AssertionError();
    }

    public static FollowerEntityDB fromFollowerEntity(FollowerEntity follower) {
        return new FollowerEntityDB(follower.id(), follower.createdAt(), follower.user().id());
    }
}
