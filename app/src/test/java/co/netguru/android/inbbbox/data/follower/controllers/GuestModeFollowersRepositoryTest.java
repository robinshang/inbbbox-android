package co.netguru.android.inbbbox.data.follower.controllers;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;
import org.greenrobot.greendao.rx.RxTransaction;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.data.dribbbleuser.user.model.db.DaoSession;
import co.netguru.android.inbbbox.data.dribbbleuser.user.model.db.UserEntityDB;
import co.netguru.android.inbbbox.data.dribbbleuser.user.model.db.UserEntityDBDao;
import co.netguru.android.inbbbox.data.follower.model.api.FollowerEntity;
import co.netguru.android.inbbbox.data.follower.model.db.FollowerEntityDB;
import co.netguru.android.inbbbox.data.follower.model.db.FollowerEntityDBDao;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GuestModeFollowersRepositoryTest {

    private static final long FOLLOWER_ID = 1;

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    DaoSession daoSession;
    @Mock
    FollowerEntityDBDao followerEntityDBDao;
    @Mock
    UserEntityDBDao userEntityDBDao;
    @Mock
    RxDao<FollowerEntityDB, Long> followerRxDao;
    @Mock
    QueryBuilder<FollowerEntityDB> followerDBQueryBuilder;
    @Mock
    RxQuery<FollowerEntityDB> followerDBRxQuery;

    @InjectMocks
    GuestModeFollowersRepository repository;

    private RxTransaction rxTransaction;

    @Before
    public void setUp() {
        rxTransaction = new RxTransaction(daoSession);
        when(daoSession.getFollowerEntityDBDao()).thenReturn(followerEntityDBDao);
        when(daoSession.getUserEntityDBDao()).thenReturn(userEntityDBDao);
        when(followerEntityDBDao.rx()).thenReturn(followerRxDao);
        when(daoSession.rxTx()).thenReturn(rxTransaction);
        doAnswer(invocation -> {
            final Runnable test = (Runnable) invocation.getArguments()[0];
            test.run();
            return null;
        }).when(daoSession).runInTx(any());
    }

    @Test
    public void shouldQueryDBWhenGettingFollowers() {
        //given
        final TestSubscriber<FollowerEntity> subscriber = new TestSubscriber<>();
        when(followerEntityDBDao.queryBuilder()).thenReturn(followerDBQueryBuilder);
        when(followerDBQueryBuilder.rx()).thenReturn(followerDBRxQuery);
        when(followerDBRxQuery.oneByOne()).thenReturn(Observable.empty());
        //when
        repository.getFollowers().subscribe(subscriber);
        //then
        verify(followerEntityDBDao).queryBuilder();
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldDeleteFromDBWhenRemovingFollower() {
        //given
        final TestSubscriber<FollowerEntity> subscriber = new TestSubscriber<>();
        when(followerRxDao.deleteByKey(any())).thenReturn(Observable.empty());
        //when
        repository.removeFollower(FOLLOWER_ID).subscribe(subscriber);
        //then
        verify(followerRxDao).deleteByKey(any());
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnErrorWhenRemovingFollowerFailed() {
        //given
        final TestSubscriber<FollowerEntity> subscriber = new TestSubscriber<>();
        when(followerRxDao.deleteByKey(any())).thenReturn(Observable.error(new Throwable()));
        //when
        repository.removeFollower(FOLLOWER_ID).subscribe(subscriber);
        //then
        subscriber.assertError(Throwable.class);
    }

    @Test
    public void shouldInsertFollowerIntoDBWhenAddingFollower() {
        //given
        final TestSubscriber<FollowerEntity> subscriber = new TestSubscriber<>();
        //when
        repository.addFollower(Statics.FOLLOWER_ENTITY).subscribe(subscriber);
        //then
        verify(followerEntityDBDao).insertOrReplace(any(FollowerEntityDB.class));
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldInsertUserEntityIntoDBWhenAddingFollower() {
        //given
        final TestSubscriber<FollowerEntity> subscriber = new TestSubscriber<>();
        //when
        repository.addFollower(Statics.FOLLOWER_ENTITY).subscribe(subscriber);
        //then
        verify(userEntityDBDao).insertOrReplace(any(UserEntityDB.class));
        subscriber.assertNoErrors();
    }
}