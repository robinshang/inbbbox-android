package co.netguru.android.inbbbox.data.follower.model.db;

import co.netguru.android.inbbbox.data.follower.model.api.FollowerEntity;

public class FollowerEntityDBMapper {

    private FollowerEntityDBMapper() {
        throw new AssertionError();
    }

    public static FollowerEntityDB fromFollowerEntity(FollowerEntity follower) {
        return new FollowerEntityDB(follower.id(), follower.createdAt(), follower.user().id());
    }
}
