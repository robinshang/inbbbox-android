package co.netguru.android.inbbbox.utils;

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class RxBus {
    private static RxBus instance;

    private final Subject<Object, Object> subject = new SerializedSubject<>(PublishSubject.create());

    public static RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }
        return instance;
    }

    public <T> Subscription register(final Class<T> eventClass, Action1<T> onNext) {
        return subject
                .compose(androidIO())
                .filter(event -> event.getClass().equals(eventClass))
                .map(obj -> (T) obj)
                .subscribe(onNext);
    }

    public void publishEvent(Object object) {
        subject.onNext(object);
    }
}
