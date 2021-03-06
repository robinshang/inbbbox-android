package co.netguru.android.inbbbox.feature.bucket.createbucket;


import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.common.utils.RxTransformerUtil;
import co.netguru.android.inbbbox.common.utils.StringUtil;
import co.netguru.android.inbbbox.data.bucket.controllers.BucketsController;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.event.events.BucketCreatedEvent;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

@FragmentScope
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
        if (StringUtil.isBlank(name)) {
            getView().showErrorEmptyBucket();
        } else if (createBucketSubscription.isUnsubscribed()) {
            createBucketSubscription = bucketsController.createBucket(name, description)
                    .compose(RxTransformerUtil.applySingleIoSchedulers())
                    .doOnSubscribe(getView()::showProgressView)
                    .doAfterTerminate(this::hideProgressViewAndCloseDialog)
                    .subscribe(bucket -> rxBus.send(new BucketCreatedEvent(bucket)),
                            throwable -> handleError(throwable, "Error occurred while creating bucket"));
        }
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
    }

    private void hideProgressViewAndCloseDialog() {
        getView().hideProgressView();
        getView().close();
    }
}
