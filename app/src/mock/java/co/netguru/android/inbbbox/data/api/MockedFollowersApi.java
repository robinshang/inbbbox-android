package co.netguru.android.inbbbox.data.api;

import java.util.List;

import co.netguru.android.inbbbox.data.models.FollowersEntity;
import rx.Observable;

public class MockedFollowersApi implements FollowersApi {
    @Override
    public Observable<List<FollowersEntity>> getFollowedUsers() {
        return null;
    }
}
