package co.netguru.android.inbbbox.model.localrepository.database.mapper;

import android.support.annotation.NonNull;

import co.netguru.android.inbbbox.model.localrepository.database.UserDB;
import co.netguru.android.inbbbox.model.ui.User;

public class UserDBMapper {

    private UserDBMapper() {
        throw new AssertionError();
    }

    public static UserDB fromUser(@NonNull User user) {
        return new UserDB(user.id(), user.name(),
                user.avatarUrl(), user.username(), user.shotsCount());
    }
}
