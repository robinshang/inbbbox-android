package co.netguru.android.inbbbox.api;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.model.api.CommentEntity;
import retrofit2.http.Path;
import rx.Observable;

public class MockCommentsApi implements ShotCommentsApi {
    @Override
    public Observable<List<CommentEntity>> getComments(@Path("shotId") String shotId) {
        return Observable.just(Collections.EMPTY_LIST);
    }
}
