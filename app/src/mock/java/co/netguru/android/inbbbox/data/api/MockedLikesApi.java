package co.netguru.android.inbbbox.data.api;

import java.util.LinkedList;
import java.util.List;

import co.netguru.android.inbbbox.data.models.ShotEntity;
import rx.Observable;

public class MockedLikesApi implements LikesApi {

    private static final List<ShotEntity> shots = new LinkedList<>();

    static {
        shots.add(new ShotEntity());
    }

    @Override
    public Observable<List<ShotEntity>> getLikedShots() {
        return Observable.just(shots);
    }
}
