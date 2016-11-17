package co.netguru.android.inbbbox.feature.details;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.ShotDetailsController;
import co.netguru.android.inbbbox.model.ui.ShotDetails;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class ShotDetailsPresenter
        extends MvpNullObjectBasePresenter<ShotDetailsContract.View>
        implements ShotDetailsContract.Presenter {

    private final long shotId;
    private final ShotDetailsController shotDetailsController;
    private final ErrorMessageController messageController;
    private final CompositeSubscription subscriptions;

    @Inject
    public ShotDetailsPresenter(long shotId,
                                ShotDetailsController shotDetailsController,
                                ErrorMessageController messageController) {
        this.shotId = shotId;
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
        subscriptions.add(
                shotDetailsController.getShotDetails(shotId)
                        .compose(androidIO())
                        .subscribe(this::handleShotDetails, this::handleApiError)
        );
//        TODO: 15.11.2016 MOCKED DATA - remove in task IA-146
//        getView().showDetails(MockedExampleData.getMocketShotDetailsData());
//        getView().showMainImage(MockedExampleData.getExampleImageUrl(), MockedExampleData.getExampleImageUrl());
        // TODO: 15.11.2016 MOCKED DATA - remove in task IA-146
    }

    private void handleShotDetails(ShotDetails shotDetails) {
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
