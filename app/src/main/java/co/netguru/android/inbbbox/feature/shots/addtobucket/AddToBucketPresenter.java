package co.netguru.android.inbbbox.feature.shots.addtobucket;


import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.BucketsController;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.RxTransformerUtils;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class AddToBucketPresenter extends MvpNullObjectBasePresenter<AddToBucketContract.View>
        implements AddToBucketContract.Presenter {

    private final BucketsController bucketsController;
    private final CompositeSubscription subscriptions;

    private Shot shot;

    @Inject
    AddToBucketPresenter(BucketsController bucketsController) {
        this.bucketsController = bucketsController;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        subscriptions.clear();
    }

    @Override
    public void handleShot(Shot shot) {
        this.shot = shot;
        getView().setShotTitle(shot.title());
        getView().showShotPreview(shot.normalImageUrl());
        getView().showAuthorAvatar(shot.author().avatarUrl());
        if (shot.team() != null) {
            getView().showShotAuthorAndTeam(shot.author().name(), shot.team().name());
        } else {
            getView().showShotAuthor(shot.author().name());
        }
        getView().showShotCreationDate(shot.creationDate());
    }

    @Override
    public void loadAvailableBuckets() {
        subscriptions.add(
                bucketsController.getCurrentUserBuckets()
                        .doOnSubscribe(getView()::showBucketListLoading)
                        .compose(RxTransformerUtils.applySingleIoSchedulers())
                        .subscribe(buckets -> {
                            if (buckets.isEmpty()) {
                                getView().showNoBucketsAvailable();
                            } else {
                                getView().showBuckets(buckets);
                            }
                        }, throwable -> {
                            Timber.d(throwable, "Error occurred while requesting buckets");
                            getView().showApiError();
                        })
        );
    }

    @Override
    public void handleBucketClick(Bucket bucket) {
        getView().passResultAndCloseFragment(bucket, shot);
    }

}
