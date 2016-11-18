package co.netguru.android.inbbbox.feature.details;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.ShotDetailsController;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.Shot;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class ShotDetailsPresenter
        extends MvpNullObjectBasePresenter<ShotDetailsContract.View>
        implements ShotDetailsContract.Presenter {

    private Shot shot;
    private final ShotDetailsController shotDetailsController;
    private final ErrorMessageController messageController;
    private final CompositeSubscription subscriptions;

    @Inject
    public ShotDetailsPresenter(Shot shot,
                                ShotDetailsController shotDetailsController,
                                ErrorMessageController messageController) {
        this.shot = shot;
        this.shotDetailsController = shotDetailsController;
        this.messageController = messageController;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void downloadData() {
        showShotDetails(shot);
        subscriptions.add(
                shotDetailsController.getShotComments(shot.id())
                        .compose(androidIO())
                        .subscribe(this::showComments, this::handleApiError)
        );
    }

    private void showComments(List<Comment> comments) {
        getView().showComments(comments);
    }

    private void showShotDetails(Shot shotDetails) {
        Timber.d("Shot details received: " + shotDetails);
        getView().showMainImage(shotDetails);
        getView().showDetails(shotDetails);
        getView().initView();
    }

    private void handleApiError(Throwable throwable) {
        Timber.e(throwable, "details download failed! ");
        // TODO: 17.11.2016 handle error
    }

}
