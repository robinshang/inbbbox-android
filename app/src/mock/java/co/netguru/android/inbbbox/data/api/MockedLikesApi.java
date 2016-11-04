package co.netguru.android.inbbbox.data.api;

import java.util.LinkedList;
import java.util.List;

import co.netguru.android.inbbbox.models.LikedShotEntity;
import co.netguru.android.inbbbox.models.ui.LikedShot;
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
}
