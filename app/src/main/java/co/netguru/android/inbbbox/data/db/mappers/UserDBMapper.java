package co.netguru.android.inbbbox.data.db.mappers;

import co.netguru.android.inbbbox.data.db.UserDB;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;

public class UserDBMapper {

    private UserDBMapper() {
        throw new AssertionError();
    }

    public static UserDB fromUser(User user) {
        return new UserDB(user.id(), user.name(),
                user.avatarUrl(), user.username(), user.shotsCount());
    }
}
