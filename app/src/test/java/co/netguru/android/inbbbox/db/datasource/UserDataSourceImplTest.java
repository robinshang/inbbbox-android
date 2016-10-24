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
    public void whenSaveMethodCalled_thenPutSettingsObjectToStorageWithSettingsKey() {
        User objectToSave = new User();
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        userDataSource.save(objectToSave).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        try {
            verify(storageMock, times(1)).put(Constants.Db.CURRENT_USER_KEY, objectToSave);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void whenGetExistingObjectFromDb_thenReturnTheObject() {
        User object = new User();
        try {
            when(storageMock.get(Constants.Db.CURRENT_USER_KEY, User.class)).thenReturn(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TestSubscriber<User> testSubscriber = new TestSubscriber<>();

        userDataSource.get().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        User result = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(object, result);
    }

    //ERRORS
    @Test
    public void whenSaveMethodFailed_thenReturnFalse() {
        try {
            doThrow(new Throwable()).when(storageMock).put(Constants.Db.CURRENT_USER_KEY, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        User objectToSave = new User();
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        userDataSource.save(objectToSave).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Assert.assertEquals(testSubscriber.getOnNextEvents().get(0), false);
    }

    @Test
    public void whenGettingNotExistingObjectFromDb_thenReturnReturnEmptyObservable() {
        try {
            doThrow(new Exception()).when(storageMock).get(Constants.Db.CURRENT_USER_KEY, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TestSubscriber<User> testSubscriber = new TestSubscriber<>();

        userDataSource.get().subscribe(testSubscriber);

        Assert.assertEquals(testSubscriber.getValueCount(), 0);
    }

}