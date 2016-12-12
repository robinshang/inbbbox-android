package co.netguru.android.inbbbox.localrepository;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;
import rx.Single;
import timber.log.Timber;

@Singleton
public class GuestModeRepository {

    private static final String LIKED_SHOTS_KEY = "key:liked_shots";
    private static final String SHOT_IS_NOT_LIKED_ERROR = "Shot is not liked";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    private Type listType;

    public GuestModeRepository(SharedPreferences sharedPreferences, Gson gson) {

        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
        listType = new TypeToken<List<Shot>>() {
        }.getType();
    }

    public Completable addLikeId(@NonNull Shot shot) {
        return getLikedShots()
                .map(shots -> addLikeIdsIfNotExist(shots, shot))
                .flatMapCompletable(this::saveLikesList);
    }

    public Completable isShotLiked(Shot shot) {
        return getLikedShots()
                .flatMapCompletable(shots -> shots.contains(shot)
                        ? Completable.complete()
                        : Completable.error(new Throwable(SHOT_IS_NOT_LIKED_ERROR)));
    }

    private Single<List<Shot>> getLikedShots() {
        return Single.fromCallable(this::getListsFromPrefs);
    }

    private List<Shot> getListsFromPrefs() {
        List<Shot> resultList = new ArrayList<>();
        String dataFromPrefs = sharedPreferences.getString(LIKED_SHOTS_KEY, "");
        Timber.d("Local likes list: " + dataFromPrefs);
        if (!dataFromPrefs.isEmpty()) {
            resultList = gson.fromJson(dataFromPrefs, listType);
        }
        return resultList;
    }

    private Completable saveLikesList(List<Shot> shots) {
        return Completable.fromCallable(() -> {
            sharedPreferences.edit()
                    .putString(LIKED_SHOTS_KEY, gson.toJson(shots))
                    .apply();
            return null;
        });
    }

    private List<Shot> addLikeIdsIfNotExist(List<Shot> shots, Shot shot) {
        if (shots != null && !shots.contains(shot)) {
            shots.add(shot);
        }
        return shots;
    }
}
