package co.netguru.android.inbbbox.data.like;

import org.threeten.bp.ZonedDateTime;

import java.util.LinkedList;
import java.util.List;

import co.netguru.android.inbbbox.data.like.model.LikedShotEntity;
import co.netguru.android.inbbbox.data.shot.model.api.Image;
import co.netguru.android.inbbbox.data.shot.model.api.ShotEntity;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Completable;
import rx.Observable;

public class MockedLikesApi implements LikesApi {

    private static final List<LikedShotEntity> shots = new LinkedList<>();

    static {
        shots.add(LikedShotEntity.builder()
                .createdAt("")
                .id(1)
                .shot(ShotEntity.builder()
                        .id(1)
                        .title("test")
                        .image(Image.builder().build())
                        .createdAt(ZonedDateTime.now())
                        .animated(false)
                        .likesCount(2)
                        .bucketsCount(3)
                        .commentsCount(2)
                        .createdAt(ZonedDateTime.now())
                        .build()
                ).build());
    }

    @Override
    public Observable<List<LikedShotEntity>> getLikedShots(@Query("page") int pageNumber, @Query("per_page") int pageCount) {
        return Observable.just(shots);
    }

    @Override
    public Completable isShotLiked(@Path("id") long id) {
        return Completable.complete();
    }

    @Override
    public Completable likeShot(@Path("id") long id) {
        return Completable.complete();
    }

    @Override
    public Completable unLikeShot(@Path("id") long id) {
        return Completable.complete();
    }
}
