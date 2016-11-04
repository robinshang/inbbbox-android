package co.netguru.android.inbbbox.data.api;

import java.util.LinkedList;
import java.util.List;

import co.netguru.android.inbbbox.data.models.FollowerEntity;
import rx.Observable;

public class MockedFollowersApi implements FollowersApi {

    private List<FollowerEntity> followers = new LinkedList<>();

    @Override
    public Observable<List<FollowerEntity>> getFollowedUsers() {
        return Observable.just(followers);
    }
}
