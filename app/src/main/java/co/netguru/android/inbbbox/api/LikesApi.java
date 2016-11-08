package co.netguru.android.inbbbox.api;

import java.util.List;

import co.netguru.android.inbbbox.model.api.LikedShotEntity;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface LikesApi {

    @GET("user/likes")
    Observable<List<LikedShotEntity>> getLikedShots();

    @POST("shots/{id}/like")
    Observable<Response<ResponseBody>> likeShot(@Path("id") long id);
}
