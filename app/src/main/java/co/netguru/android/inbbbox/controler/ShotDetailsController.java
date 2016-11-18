package co.netguru.android.inbbbox.controler;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.api.ShotCommentsApi;
import co.netguru.android.inbbbox.model.ui.Comment;
import rx.Observable;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

public class ShotDetailsController {

    private final ShotCommentsApi shotCommentsApi;

    @Inject
    public ShotDetailsController(ShotCommentsApi shotCommentsApi) {
        this.shotCommentsApi = shotCommentsApi;
    }

    public Observable<List<Comment>> getShotComments(Long shotId) {
        return getShotComments(shotId.toString());
    }

    private Observable<List<Comment>> getShotComments(String shotId) {
        return shotCommentsApi.getComments(shotId)
                .compose(fromListObservable())
                .map(Comment::create)
                .distinct()
                .toList();
    }
}
