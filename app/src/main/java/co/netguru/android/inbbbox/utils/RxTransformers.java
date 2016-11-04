package co.netguru.android.inbbbox.utils;


import rx.Completable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxTransformers {

    private RxTransformers() {
        throw new AssertionError();
    }

    public static <T> Single.Transformer<T, T> applySingleIoSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable.CompletableTransformer applyCompletableIoSchedulers() {
        return completable -> completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
