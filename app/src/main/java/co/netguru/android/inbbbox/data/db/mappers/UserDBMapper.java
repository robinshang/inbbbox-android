package co.netguru.android.inbbbox.data.db.mappers;

import android.support.annotation.NonNull;

import co.netguru.android.inbbbox.data.db.UserDB;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;

public class UserDBMapper {

    private UserDBMapper() {
        throw new AssertionError();
    }

    public static UserDB fromUser(@NonNull User user) {
        return new UserDB(user.id(), user.name(), user.avatarUrl(), user.username(),
                user.shotsCount(), user.bucketsCount(), user.projectsCount(), user.type());
    }
}
