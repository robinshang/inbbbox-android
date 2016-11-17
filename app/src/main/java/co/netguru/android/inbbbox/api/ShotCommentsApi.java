package co.netguru.android.inbbbox.api;

import java.util.List;

import co.netguru.android.inbbbox.model.api.CommentEntity;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

@FunctionalInterface
public interface ShotCommentsApi {

    @GET("shots/{shotId}/comments")
    Observable<List<CommentEntity>> getComments(@Path("shotId") String shotId);
}
