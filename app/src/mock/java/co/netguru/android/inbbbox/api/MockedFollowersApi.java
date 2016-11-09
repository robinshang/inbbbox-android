package co.netguru.android.inbbbox.api;

import java.util.LinkedList;
import java.util.List;

import co.netguru.android.inbbbox.api.FollowersApi;
import co.netguru.android.inbbbox.model.api.FollowerEntity;
import rx.Observable;

public class MockedFollowersApi implements FollowersApi {

    private List<FollowerEntity> followers = new LinkedList<>();

    @Override
    public Observable<List<FollowerEntity>> getFollowedUsers() {
        return Observable.just(followers);
    }
}
