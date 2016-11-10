package co.netguru.android.inbbbox.api;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.model.api.FollowerEntity;
import rx.Observable;

public class MockedFollowersApi implements FollowersApi {

    private final List<FollowerEntity> followers = Collections.emptyList();

    @Override
    public Observable<List<FollowerEntity>> getFollowedUsers() {
        return Observable.just(followers);
    }
}
