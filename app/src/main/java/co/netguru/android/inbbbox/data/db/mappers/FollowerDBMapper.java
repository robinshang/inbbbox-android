package co.netguru.android.inbbbox.data.db.mappers;

import co.netguru.android.inbbbox.data.db.FollowerDB;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;

public class FollowerDBMapper {

    private FollowerDBMapper() {
        throw new AssertionError();
    }

    public static FollowerDB fromUser(User follower) {
        return new FollowerDB(follower.id());
    }
}
