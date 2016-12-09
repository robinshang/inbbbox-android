package co.netguru.android.inbbbox.localrepository;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import javax.inject.Singleton;

import co.netguru.android.inbbbox.model.localrepository.GuestModeShotsHolder;
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

    public GuestModeRepository(SharedPreferences sharedPreferences, Gson gson) {

        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    public Completable addLikeId(@NonNull Shot shot) {
        return getLikedShots()
                .map(holder -> addLikeIdsIfNotExist(holder, shot))
                .flatMapCompletable(this::saveLikesList);
    }

    public Completable isShotLiked(long id) {
        return getLikedShots()
                .flatMapCompletable(holder -> holder.getIdsList().contains(id)
                        ? Completable.complete()
                        : Completable.error(new Throwable(SHOT_IS_NOT_LIKED_ERROR)));
    }

    private Single<GuestModeShotsHolder> getLikedShots() {
        return Single.fromCallable(this::getListsFromPrefs);
    }

    private GuestModeShotsHolder getListsFromPrefs() {
        GuestModeShotsHolder resultList = new GuestModeShotsHolder();
        String dataFromPrefs = sharedPreferences.getString(LIKED_SHOTS_KEY, "");
        Timber.d("Local likes list: " + dataFromPrefs);
        if (!dataFromPrefs.isEmpty()) {
            resultList = gson.fromJson(dataFromPrefs, GuestModeShotsHolder.class);
        }
        return resultList;
    }

    private Completable saveLikesList(GuestModeShotsHolder holder) {
        return Completable.fromCallable(() -> {
            sharedPreferences.edit()
                    .putString(LIKED_SHOTS_KEY, gson.toJson(holder))
                    .apply();
            return null;
        });
    }

    private GuestModeShotsHolder addLikeIdsIfNotExist(GuestModeShotsHolder holder, Shot shot) {
        if (holder != null && !holder.getIdsList().contains(shot.id())) {
            holder.getIdsList().add(shot.id());
            holder.getShotList().add(shot);
        }
        return holder;
    }
}
