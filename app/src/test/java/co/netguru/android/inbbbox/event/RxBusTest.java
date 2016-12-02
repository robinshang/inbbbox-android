package co.netguru.android.inbbbox.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import rx.observers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
public class RxBusTest {

    @InjectMocks
    RxBus rxBus;

    private static final TestEventFirstType TEST_EVENT_FIRST_TYPE = new TestEventFirstType();
    private static final List<TestEventFirstType> FIRST_EVENT_TYPE_SINGLETON_LIST =
            Collections.singletonList(TEST_EVENT_FIRST_TYPE);

    @Test
    public void whenSubscribedAfterSendForGivenType_thenReceiveNoEvents() throws Exception {
        //given
        TestSubscriber<TestEventFirstType> testSubscriber = TestSubscriber.create();
        //when
        rxBus.send(TEST_EVENT_FIRST_TYPE);

        rxBus.getEvents(TestEventFirstType.class)
                .subscribe(testSubscriber);
        //then
        testSubscriber.assertNoValues();
    }

    @Test
    public void whenSubscribedAfterSendNotForGivenType_thenReceiveNoEvents() throws Exception {
        //given
        TestSubscriber<TestEventSecondType> testSubscriber = TestSubscriber.create();
        //when
        rxBus.send(TEST_EVENT_FIRST_TYPE);

        rxBus.getEvents(TestEventSecondType.class)
                .subscribe(testSubscriber);
        //then
        testSubscriber.assertNoValues();
    }

    @Test
    public void whenSubscribedBeforeSendForGivenType_thenReceiveEvent() throws Exception {
        //given
        TestSubscriber<TestEventFirstType> testSubscriber = TestSubscriber.create();
        //when
        rxBus.getEvents(TestEventFirstType.class)
                .subscribe(testSubscriber);

        rxBus.send(TEST_EVENT_FIRST_TYPE);
        //then
        testSubscriber.assertReceivedOnNext(FIRST_EVENT_TYPE_SINGLETON_LIST);
    }

    @Test
    public void whenSubscribedBeforeSendNotForGivenType_thenReceiveNoEvents() throws Exception {
        //given
        TestSubscriber<TestEventSecondType> testSubscriber = TestSubscriber.create();
        //when
        rxBus.getEvents(TestEventSecondType.class)
                .subscribe(testSubscriber);

        rxBus.send(TEST_EVENT_FIRST_TYPE);
        //then
        testSubscriber.assertNoValues();
    }

    private static class TestEventFirstType implements Event {

    }

    private static class TestEventSecondType implements Event {

    }

}