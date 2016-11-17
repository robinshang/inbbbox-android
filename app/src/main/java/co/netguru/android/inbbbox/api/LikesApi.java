package co.netguru.android.inbbbox.api;

import java.util.List;

import co.netguru.android.inbbbox.model.api.LikedShotEntity;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Completable;
import rx.Observable;

public interface LikesApi {

    @GET("user/likes")
    Observable<List<LikedShotEntity>> getLikedShots(@Query("page") int pageNumber, @Query("per_page") int pageCount);

    @POST("shots/{id}/like")
    Completable likeShot(@Path("id") long id);
}
