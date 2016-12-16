package co.netguru.android.inbbbox.localrepository;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import co.netguru.android.inbbbox.model.api.FollowerEntity;
import rx.Completable;
import rx.Observable;
import timber.log.Timber;

public class GuestModeFollowersRepository {

    private static final String FOLLOWERS_DATA = "followersData";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    private final Type type;

    public GuestModeFollowersRepository(SharedPreferences sharedPreferences, Gson gson) {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
        type = new TypeToken<List<FollowerEntity>>() {
        }.getType();
    }

    public Completable removeFollower(long id) {
        return getFollowers()
                .toList()
                .doOnNext(followers -> removeIfExists(followers, id))
                .toCompletable();
    }

    public Observable<FollowerEntity> getFollowers() {
        Timber.d("Getting followers from local repository");
        final String repositoryData = sharedPreferences.getString(FOLLOWERS_DATA, "");
        if (!repositoryData.isEmpty()) {
            final List<FollowerEntity> data = gson.fromJson(repositoryData, type);
            return Observable.from(data);
        }
        return Observable.empty();
    }

    private void removeIfExists(List<FollowerEntity> followers, long id) {
        for (final FollowerEntity follower : followers) {
            if (follower.id() == id) {
                Timber.d("Removing follower fromm local repository");
                followers.remove(follower);
                saveFollowers(followers);
                break;
            }
        }
    }

    private void saveFollowers(List<FollowerEntity> followers) {
        sharedPreferences.edit()
                .putString(FOLLOWERS_DATA, gson.toJson(followers))
                .apply();
    }
}
