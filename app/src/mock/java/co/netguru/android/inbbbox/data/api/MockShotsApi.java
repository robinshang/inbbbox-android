package co.netguru.android.inbbbox.data.api;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.data.models.FilteredShotsParams;
import co.netguru.android.inbbbox.data.models.ShotEntity;
import co.netguru.android.inbbbox.data.models.User;
import retrofit2.http.Body;
import rx.Observable;

public class MockShotsApi implements ShotsApi {

    private int mockListItemsCount;

    public MockShotsApi(int mockListItemsCount) {

        this.mockListItemsCount = mockListItemsCount;
    }

    @Override
    public Observable<List<ShotEntity>> getFilteredShots(@Body FilteredShotsParams shotsParams) {
        return Observable.just(getFollowingMock(mockListItemsCount));
    }

    @Override
    public Observable<List<ShotEntity>> getFollowingShots() {
        return Observable.just(getFollowingMock(mockListItemsCount));
    }

    private List<ShotEntity> getFollowingMock(int count) {
        List<ShotEntity> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            ShotEntity entity = new ShotEntity();
            entity.setTitle("following: " + i);
            result.add(entity);
        }
        return result;
    }

    public List<ShotEntity> getMockedData() {
        return getFollowingMock(mockListItemsCount);
    }
}
