package co.netguru.android.inbbbox.controler;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.api.ShotCommentsApi;
import co.netguru.android.inbbbox.api.ShotDetailsApi;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.ShotDetails;
import rx.Observable;

import static co.netguru.android.commons.rx.RxTransformers.fromListObservable;

public class ShotDetailsController {

    private final ShotDetailsApi shotDetailsApi;
    private final ShotCommentsApi shotCommentsApi;

    @Inject
    public ShotDetailsController(ShotDetailsApi shotDetailsApi, ShotCommentsApi shotCommentsApi) {
        this.shotDetailsApi = shotDetailsApi;
        this.shotCommentsApi = shotCommentsApi;
    }

    public Observable<ShotDetails> getShotDetails(Long shotId) {
        return Observable.zip(shotDetailsApi.getShot(shotId.toString()),
                getComments(shotId.toString()),
                ShotDetails::createDetails);
    }

    private Observable<List<Comment>> getComments(String shotId) {
        return shotCommentsApi.getComments(shotId)
                .compose(fromListObservable())
                .map(Comment::create)
                .distinct()
                .toList();
    }
}
