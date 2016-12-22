package co.netguru.android.inbbbox.model.localrepository.database.mapper;

import co.netguru.android.inbbbox.model.api.FollowerEntity;
import co.netguru.android.inbbbox.model.localrepository.database.FollowerDB;

public class FollowerDBMapper {

    private FollowerDBMapper() {
        throw new AssertionError();
    }

    public static FollowerDB fromFollowerEntity(FollowerEntity follower) {
        return new FollowerDB(follower.id(), follower.createdAt(), follower.user().id());
    }
}
