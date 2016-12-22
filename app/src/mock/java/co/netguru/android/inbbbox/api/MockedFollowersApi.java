package co.netguru.android.inbbbox.api;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.model.api.FollowerEntity;
import okhttp3.Protocol;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Completable;
import rx.Observable;

public class MockedFollowersApi implements FollowersApi {

    private final List<FollowerEntity> followers = Collections.emptyList();

    @Override
    public Observable<List<FollowerEntity>> getFollowedUsers(@Query("page") int pageNumber,
                                                             @Query("per_page") int pageCount) {
        return Observable.just(followers);
    }

    @Override
    public Completable unFollowUser(@Path("user") long id) {
        return Completable.complete();
    }

    @Override
    public Completable followUser(@Path("user") long id) {
        return Completable.complete();
    }

    @Override
    public Observable<Response<Completable>> checkIfUserIsFollowed(@Path("user") long id) {
        okhttp3.Response rawResponse = new okhttp3.Response.Builder()
                .code(204)
                .protocol(Protocol.HTTP_1_1)
                .request(new Request.Builder().url("http://localhost/").build())
                .build();
        Response<Completable> response = Response.success(Completable.complete(), rawResponse);
        return Observable.just(response);
    }
}