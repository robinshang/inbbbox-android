package co.netguru.android.inbbbox.api.updatedrxcalladapter;


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Completable;
import rx.CompletableSubscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.subscriptions.Subscriptions;

/**
 * UpdatedRxJavaCallAdapter is a fast fix for changes in naming introduced in rxjava > 1.1.9, which
 * are not supported in rxjava call adapter 2.1.0 version. Most code is copy pasted from source.
 * todo 15.12.16 This class should be removed as soon as new retrofit adapter is released - newer than 2.1.0,
 * repo already contain fix for rxjava naming changes.
 */
public class UpdatedRxJavaCallAdapter extends CallAdapter.Factory {

    private final RxJavaCallAdapterFactory rxJavaCallAdapterFactory;

    private UpdatedRxJavaCallAdapter() {
        rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    }

    public static UpdatedRxJavaCallAdapter create() {
        return new UpdatedRxJavaCallAdapter();
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        String canonicalName = rawType.getCanonicalName();
        boolean isCompletable = "rx.Completable".equals(canonicalName);
        if (isCompletable) {
            return CompletableHelper.createCallAdapter();
        } else {
            return rxJavaCallAdapterFactory.get(returnType, annotations, retrofit);
        }
    }

    private static final class CompletableHelper {

        private CompletableHelper() {
            throw new AssertionError();
        }

        static CallAdapter<Completable> createCallAdapter() {
            return new CompletableHelper.CompletableCallAdapter();
        }

        private static final class CompletableCallOnSubscribe implements Completable.OnSubscribe {
            private final Call originalCall;

            CompletableCallOnSubscribe(Call originalCall) {
                this.originalCall = originalCall;
            }

            @Override
            public void call(CompletableSubscriber subscriber) {
                // Since Call is a one-shot type, clone it for each new subscriber.
                final Call call = originalCall.clone();

                // Attempt to cancel the call if it is still in-flight on unsubscription.
                Subscription subscription = Subscriptions.create(() -> call.cancel());
                subscriber.onSubscribe(subscription);

                try {
                    Response response = call.execute();
                    if (!subscription.isUnsubscribed()) {
                        if (response.isSuccessful()) {
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new HttpException(response));
                        }
                    }
                } catch (Throwable t) {
                    Exceptions.throwIfFatal(t);
                    if (!subscription.isUnsubscribed()) {
                        subscriber.onError(t);
                    }
                }
            }
        }

        static class CompletableCallAdapter implements CallAdapter<Completable> {

            @Override
            public Type responseType() {
                return Void.class;
            }

            @Override
            public Completable adapt(Call call) {
                return Completable.create(
                        new CompletableCallOnSubscribe(call));
            }
        }
    }

}
