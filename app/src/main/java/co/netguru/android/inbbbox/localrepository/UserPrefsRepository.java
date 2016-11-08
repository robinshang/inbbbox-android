package co.netguru.android.inbbbox.localrepository;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.model.api.User;
import co.netguru.android.inbbbox.utils.StringUtils;
import rx.Completable;
import rx.Single;

@Singleton
public class UserPrefsRepository {

    private static final String USER_KEY = "user";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    @Inject
    public UserPrefsRepository(SharedPreferences sharedPreferences, Gson gson) {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    public Completable saveUser(@NonNull User user) {
        return Completable.fromCallable(() -> {
            sharedPreferences.edit()
                    .putString(USER_KEY, gson.toJson(user))
                    .apply();
            return null;
        });
    }

    /**
     * Return Single<User> in case user is not found finish with UserNotFoundException in onError.
     */
    public Single<User> getUser() {
        return Single.fromCallable(() -> {
            String userJson = sharedPreferences.getString(USER_KEY, null);
            if (StringUtils.isBlank(userJson)) {
                throw new UserNotFoundException("User was not found in shared preferences");
            } else {
                return gson.fromJson(userJson, User.class);
            }
        });
    }

    public static class UserNotFoundException extends IOException {

        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
