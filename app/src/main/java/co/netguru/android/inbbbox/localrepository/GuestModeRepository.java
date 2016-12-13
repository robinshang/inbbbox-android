package co.netguru.android.inbbbox.localrepository;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.model.ui.Shot;
import rx.Completable;
import rx.Observable;
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
        listType = new TypeToken<HashMap<Long, Shot>>() {
        }.getType();
    }

    public Completable addLikedShot(@NonNull Shot shot) {
        return getLikedShotsMap()
                .map(shots -> addLikeIdsIfNotExist(shots, shot))
                .flatMapCompletable(this::saveLikesList);
    }

    public Completable isShotLiked(Shot shot) {
        return getLikedShotsMap()
                .flatMapCompletable(shots -> shots.containsKey(shot.id())
                        ? Completable.complete()
                        : Completable.error(new Throwable(SHOT_IS_NOT_LIKED_ERROR)));
    }

    public Completable removeLikedShot(Shot shot) {
        return getLikedShotsMap()
                .map(shots -> removeShotIfExist(shots, shot))
                .flatMapCompletable(this::saveLikesList);
    }

    public Observable<List<Shot>> getLikedShots() {
        return getLikedShotsMap()
                .flatMapObservable(shotsMap -> Observable.just(new ArrayList<>(shotsMap.values())));
    }

    private HashMap<Long, Shot> removeShotIfExist(HashMap<Long, Shot> shots, Shot shotToRemove) {
        if (shots.containsKey(shotToRemove.id())) {
            shots.remove(shotToRemove.id());
        }
        return shots;
    }

    private Single<HashMap<Long, Shot>> getLikedShotsMap() {
        return Single.fromCallable(this::getListsFromPrefs);
    }

    private HashMap<Long, Shot> getListsFromPrefs() {
        HashMap<Long, Shot> resultList = new HashMap<>();
        String dataFromPrefs = sharedPreferences.getString(LIKED_SHOTS_KEY, "");
        Timber.d("Local likes list: " + dataFromPrefs);
        if (!dataFromPrefs.isEmpty()) {
            resultList = gson.fromJson(dataFromPrefs, listType);
        }
        return resultList;
    }

    private Completable saveLikesList(HashMap<Long, Shot> shots) {
        return Completable.fromCallable(() -> {
            sharedPreferences.edit()
                    .putString(LIKED_SHOTS_KEY, gson.toJson(shots))
                    .apply();
            return null;
        });
    }

    private HashMap<Long, Shot> addLikeIdsIfNotExist(HashMap<Long, Shot> shots, Shot shot) {
        if (shots != null && !shots.containsKey(shot.id())) {
            shots.put(shot.id(), shot);
        }
        return shots;
    }

}
