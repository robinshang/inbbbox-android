package co.netguru.android.inbbbox.common.utils;


import java.util.concurrent.TimeUnit;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxTransformerUtil {

    private RxTransformerUtil() {
        throw new AssertionError();
    }

    public static <T> Single.Transformer<T, T> applySingleIoSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Single.Transformer<T, T> applySingleComputationSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable.Transformer applyCompletableIoSchedulers() {
        return completable -> completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Observable transformer with execute given actions if observable didn't emit anything till given
     * time from subscription.
     */
    public static <T> Observable.Transformer<T, T> executeRunnableIfObservableDidntEmitUntilGivenTime(
            int time, TimeUnit timeUnit, Runnable runnable) {
        return observable ->
                observable.publish(publishedObservable -> publishedObservable.<T>timeout(time, timeUnit,
                        Observable.<T>fromCallable(() -> {
                            runnable.run();
                            return null;
                        })
                                .ignoreElements()
                                .mergeWith(publishedObservable))
                );
    }

    public static <T> Observable.Transformer<T, T> androidComputation() {
        return observable -> observable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
