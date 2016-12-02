package co.netguru.android.inbbbox.event;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

@Singleton
public class RxBus {

    private final Subject<Event, Event> subject;

    @Inject
    public RxBus() {
        subject = new SerializedSubject<>(PublishSubject.create());
    }

    public <T extends Event> Observable<T> getEvents(final Class<T> eventClass) {
        return subject.ofType(eventClass);
    }

    public void send(Event event) {
        subject.onNext(event);
    }
}
