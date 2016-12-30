package co.netguru.android.inbbbox.data.like;

import java.util.List;

import co.netguru.android.inbbbox.data.like.model.LikedShotEntity;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Completable;
import rx.Observable;

public interface LikesApi {

    @GET("user/likes")
    Observable<List<LikedShotEntity>> getLikedShots(@Query("page") int pageNumber, @Query("per_page") int pageCount);

    @GET("shots/{id}/like")
    Completable isShotLiked(@Path("id") long id);

    @POST("shots/{id}/like")
    Completable likeShot(@Path("id") long id);

    @DELETE("shots/{id}/like")
    Completable unLikeShot(@Path("id") long id);
}
