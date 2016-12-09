package co.netguru.android.inbbbox.localrepository;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import rx.Completable;
import rx.Single;
import timber.log.Timber;

@Singleton
public class GuestModeRepository {

    private static final String LIKES_LIST_KEY = "key:likes";
    private static final String SHOT_IS_NOT_LIKED_ERROR = "Shot is not liked";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    private final Type listType;

    public GuestModeRepository(SharedPreferences sharedPreferences, Gson gson) {

        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
        this.listType = new TypeToken<List<Long>>() {
        }.getType();
    }

    public Completable addLikeId(@NonNull Long likeId) {
        return getLikesList()
                .map(likeIds -> addLikeIdsIfNotExist(likeIds, likeId))
                .flatMapCompletable(this::saveLikesList);
    }

    public Completable isShotLiked(long id) {
        return getLikesList()
                .flatMapCompletable(longs -> longs.contains(id)
                        ? Completable.complete()
                        : Completable.error(new Throwable(SHOT_IS_NOT_LIKED_ERROR)));
    }

    private Single<List<Long>> getLikesList() {
        return Single.fromCallable(this::getListFromPrefs
        );
    }

    private List<Long> getListFromPrefs() {
        List<Long> resultList = new ArrayList<>();
        String list = sharedPreferences.getString(LIKES_LIST_KEY, "");
        Timber.d("Local likes list: " + list);
        if (!list.isEmpty()) {
            resultList = gson.fromJson(list, listType);
        }
        return resultList;
    }

    private Completable saveLikesList(List<Long> listToSave) {
        return Completable.fromCallable(() -> {
            sharedPreferences.edit()
                    .putString(LIKES_LIST_KEY, gson.toJson(listToSave))
                    .apply();
            return null;
        });
    }

    private List<Long> addLikeIdsIfNotExist(List<Long> likesList, Long likeId) {
        if (likesList != null && !likesList.contains(likeId)) {
            likesList.add(likeId);
        }
        return likesList;
    }
}
