package co.netguru.android.inbbbox.data.follower;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.data.follower.model.api.FollowerEntity;
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
}