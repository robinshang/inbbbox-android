package co.netguru.android.inbbbox.feature.details;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.ShotDetailsController;
import rx.subscriptions.CompositeSubscription;

public class ShotDetailsPresenter
        extends MvpNullObjectBasePresenter<ShotDetailsContract.View>
        implements ShotDetailsContract.Presenter {

    private CompositeSubscription subscriptions;
    private ShotDetailsController shotDetailsController;

    @Inject
    ShotDetailsPresenter(ShotDetailsController shotDetailsController) {

        this.shotDetailsController = shotDetailsController;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void downloadData() {
//        TODO: 15.11.2016 MOCKED DATA - remove in task IA-146
        getView().showItems(MockedExampleData.getMocketShotDetailsData());
        getView().showMainImage(MockedExampleData.getExampleImageUrl(), MockedExampleData.getExampleImageUrl());
        // TODO: 15.11.2016 MOCKED DATA - remove in task IA-146
    }

}
