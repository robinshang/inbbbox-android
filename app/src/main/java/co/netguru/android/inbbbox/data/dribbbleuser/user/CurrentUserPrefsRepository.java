package co.netguru.android.inbbbox.data.dribbbleuser.user;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.common.utils.StringUtil;
import co.netguru.android.inbbbox.data.dribbbleuser.user.model.api.UserEntity;
import rx.Completable;
import rx.Single;

@Singleton
public class CurrentUserPrefsRepository {

    private static final String USER_KEY = "user";
    private static final String GUEST_MODE_STATE_KEY = "guest_mode";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    @Inject
    public CurrentUserPrefsRepository(SharedPreferences sharedPreferences, Gson gson) {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    public Completable saveUser(@NonNull UserEntity user) {
        return Completable.fromAction(() ->
            sharedPreferences.edit()
                    .putString(USER_KEY, gson.toJson(user))
                    .apply()
            );
    }

    /**
     * Return Single<UserEntity> in case user is not found finish with UserNotFoundException in onError.
     */
    public Single<UserEntity> getUser() {
        return Single.fromCallable(() -> {
            String userJson = sharedPreferences.getString(USER_KEY, null);
            if (StringUtil.isBlank(userJson)) {
                throw new UserNotFoundException("UserEntity was not found in shared preferences");
            } else {
                return gson.fromJson(userJson, UserEntity.class);
            }
        });
    }

    public Completable setGuestModeEnabled(boolean guestModeEnabled) {
        return Completable.fromAction(() ->
            sharedPreferences.edit()
                    .putBoolean(GUEST_MODE_STATE_KEY, guestModeEnabled)
                    .apply()
        );
    }

    public Single<Boolean> isGuestModeEnabled() {
        return Single.fromCallable(() ->
                sharedPreferences.getBoolean(GUEST_MODE_STATE_KEY, false)
        );
    }

    public Completable clear() {
        return Completable.fromAction(() -> sharedPreferences.edit().clear().apply());
    }

    public static class UserNotFoundException extends IOException {

        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
