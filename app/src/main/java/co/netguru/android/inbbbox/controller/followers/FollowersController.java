package co.netguru.android.inbbbox.controller.followers;

import co.netguru.android.inbbbox.model.api.FollowerEntity;
import rx.Completable;
import rx.Observable;

public interface FollowersController {

    Observable<FollowerEntity> getFollowedUsers(int pageNumber, int pageCount);

    Completable unFollowUser(long id);
}
