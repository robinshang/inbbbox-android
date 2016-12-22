package co.netguru.android.inbbbox.controler.followers;

import co.netguru.android.inbbbox.model.api.FollowerEntity;
import retrofit2.Response;
import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FollowersController {

    Observable<FollowerEntity> getFollowedUsers(int pageNumber, int pageCount);

    Completable unFollowUser(long id);

    Completable followUser(long id);

    Single<Response<Void>> checkIfUserIsFollowed(long id);
}
