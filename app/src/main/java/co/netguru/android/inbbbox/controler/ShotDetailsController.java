package co.netguru.android.inbbbox.controler;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.api.LikesApi;
import co.netguru.android.inbbbox.api.ShotCommentsApi;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.ShotDetailsState;
import rx.Observable;
import rx.functions.Func1;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

public class ShotDetailsController {

    private final ShotCommentsApi shotCommentsApi;
    private LikesApi likesApi;

    @Inject
    public ShotDetailsController(ShotCommentsApi shotCommentsApi, LikesApi likesApi) {
        this.shotCommentsApi = shotCommentsApi;
        this.likesApi = likesApi;
    }

    public Observable<ShotDetailsState> getShotComments(Long shotId) {
        return Observable.zip(getCommentList(shotId.toString()),
                getLikeState(shotId),
                (comments, isLiked) -> new ShotDetailsState(isLiked, false, comments));
    }

    private Observable<List<Comment>> getCommentList(String shotId) {
        return shotCommentsApi.getComments(shotId)
                .compose(fromListObservable())
                .map(Comment::create)
                .distinct()
                .toList()
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<Comment>>>() {
                    @Override
                    public Observable<? extends List<Comment>> call(Throwable throwable) {
                        return Observable.just(Collections.emptyList());
                    }
                });
    }

    private Observable<Boolean> getLikeState(Long shotId) {
        return likesApi.isShotLiked(shotId)
                .andThen(Observable.just(Boolean.TRUE))
                .onErrorResumeNext(Observable.just(Boolean.FALSE));
    }
}
