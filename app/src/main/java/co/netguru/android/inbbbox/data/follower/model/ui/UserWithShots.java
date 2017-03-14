package co.netguru.android.inbbbox.data.follower.model.ui;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

import co.netguru.android.inbbbox.data.Cacheable;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;

@AutoValue
public abstract class UserWithShots implements Parcelable, Cacheable {

    public abstract User user();

    @Nullable
    public abstract List<Shot> shotList();

    public static UserWithShots create(@NonNull User user, @Nullable List<Shot> shotList) {
        return new AutoValue_UserWithShots(user, shotList);
    }

    @Override
    public long getId() {
        return user().id();
    }
}
