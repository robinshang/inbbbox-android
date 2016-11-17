package co.netguru.android.inbbbox.api;

import co.netguru.android.inbbbox.model.api.CommentEntity;
import retrofit2.http.Path;
import rx.Observable;

public class MockCommentsApi implements ShotCommentsApi {
    @Override
    public Observable<CommentEntity> getComments(@Path("shotId") String shotId) {
        return Observable.just(new CommentEntity());
    }
}
