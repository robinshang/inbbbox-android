package co.netguru.android.inbbbox.feature.buckets.createbucket;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.buckets.BucketsController;
import co.netguru.android.inbbbox.controler.ErrorController;
import co.netguru.android.inbbbox.event.BucketCreatedEvent;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.utils.RxTransformerUtils;
import co.netguru.android.inbbbox.utils.StringUtils;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class CreateBucketPresenter extends MvpNullObjectBasePresenter<CreateBucketContract.View>
        implements CreateBucketContract.Presenter {

    private final BucketsController bucketsController;
    private final ErrorController errorController;
    private final RxBus rxBus;
    @NonNull
    private Subscription createBucketSubscription;

    @Inject
    CreateBucketPresenter(BucketsController bucketsController, ErrorController errorController, RxBus rxBus) {
        this.bucketsController = bucketsController;
        this.errorController = errorController;
        this.rxBus = rxBus;
        createBucketSubscription = Subscriptions.unsubscribed();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        createBucketSubscription.unsubscribe();
    }

    @Override
    public void handleCreateBucket(String name, String description) {
        getView().hideErrorMessages();
        if (StringUtils.isBlank(name)) {
            getView().showErrorEmptyBucket();
        } else if (createBucketSubscription.isUnsubscribed()) {
            createBucketSubscription = bucketsController.createBucket(name, description)
                    .compose(RxTransformerUtils.applySingleIoSchedulers())
                    .doOnSubscribe(getView()::showProgressView)
                    .doAfterTerminate(getView()::hideProgressView)
                    .subscribe(bucket -> {
                                rxBus.send(new BucketCreatedEvent(bucket));
                                getView().close();
                            },
                            throwable -> Timber.d(throwable, "Error occurred while creating bucket"));
        }
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }
}
