package co.netguru.android.inbbbox.db.datasource;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.data.models.User;
import co.netguru.android.inbbbox.db.Storage;
import co.netguru.android.inbbbox.utils.Constants;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDataSourceImplTest {
    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public Storage storageMock;

    @InjectMocks
    public UserDataSourceImpl userDataSource;

    @Test
    public void whenSaveMethodCalled_thenPutSettingsObjectToStorageWithSettingsKey() throws Exception {
        User objectToSave = new User();
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        userDataSource.save(objectToSave).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        verify(storageMock, times(1)).put(Constants.Db.CURRENT_USER_KEY, objectToSave);
    }

    @Test
    public void whenObjectSaved_thenReturnTrueAsResult() {
        User objectToSave = new User();
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        userDataSource.save(objectToSave).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Assert.assertEquals(testSubscriber.getOnNextEvents().get(0), true);
    }

    @Test
    public void whenGetExistingObjectFromDb_thenReturnTheObject() throws Exception {
        User object = new User();
        when(storageMock.get(Constants.Db.CURRENT_USER_KEY, User.class)).thenReturn(object);
        TestSubscriber<User> testSubscriber = new TestSubscriber<>();

        userDataSource.get().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        User result = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(object, result);
    }

    //ERRORS
    @Test
    public void whenSaveMethodFailed_thenReturnFalse() throws Exception {
        doThrow(new Throwable()).doCallRealMethod();
        User objectToSave = new User();
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        userDataSource.save(objectToSave).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Assert.assertEquals(testSubscriber.getOnNextEvents().get(0), false);
    }

    @Test
    public void whenGettingNotExistingObjectFromDb_thenReturnReturnEmptyObservable() throws Exception {
        doThrow(new Exception()).when(storageMock).get(Constants.Db.CURRENT_USER_KEY, User.class);
        TestSubscriber<User> testSubscriber = new TestSubscriber<>();

        userDataSource.get().subscribe(testSubscriber);

        Assert.assertEquals(testSubscriber.getValueCount(), 0);
    }

}