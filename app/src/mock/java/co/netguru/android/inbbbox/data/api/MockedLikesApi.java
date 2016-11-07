package co.netguru.android.inbbbox.data.api;

import java.util.LinkedList;
import java.util.List;

import co.netguru.android.inbbbox.models.LikedShotEntity;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Path;
import rx.Observable;

public class MockedLikesApi implements LikesApi {

    private static final List<LikedShotEntity> shots = new LinkedList<>();

    static {
        shots.add(LikedShotEntity.builder().build());
    }

    @Override
    public Observable<List<LikedShotEntity>> getLikedShots() {
        return Observable.just(shots);
    }

    @Override
    public Observable<Response<ResponseBody>> likeShot(@Path("id") long id) {
        return Observable.just(Response.success(ResponseBody.create(MediaType.parse(""), "success")));
    }
}
