package co.netguru.android.inbbbox.localrepository.database;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDB;
import co.netguru.android.inbbbox.model.localrepository.database.ShotDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.TeamDB;
import co.netguru.android.inbbbox.model.localrepository.database.TeamDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.UserDB;
import co.netguru.android.inbbbox.model.localrepository.database.UserDBDao;
import co.netguru.android.inbbbox.model.localrepository.database.mapper.ShotDBMapper;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GuestModeLikesRepositoryTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    ShotDBDao shotDBDao;
    @Mock
    UserDBDao userDBDao;
    @Mock
    TeamDBDao teamDBDao;
    @Mock
    RxDao<ShotDB, Long> shotRxDao;
    @Mock
    RxDao<UserDB, Long> userRxDao;
    @Mock
    RxDao<TeamDB, Long> teamRxDao;
    @Mock
    QueryBuilder<ShotDB> shotDBQueryBuilder;
    @Mock
    RxQuery<ShotDB> shotDBRxQuery;

    @InjectMocks
    GuestModeLikesRepository repository;

    private final ShotDB shotDB = ShotDBMapper.fromShot(Statics.NOT_LIKED_SHOT);
    private final UserDB userDB = new UserDB();
    private final TeamDB teamDB = new TeamDB();

    @Before
    public void setUp() {
        when(shotDBDao.rx()).thenReturn(shotRxDao);
        when(shotRxDao.insertOrReplace(any())).thenReturn(Observable.just(shotDB));
        when(userDBDao.rx()).thenReturn(userRxDao);
        when(userRxDao.insertOrReplace(any())).thenReturn(Observable.just(userDB));
        when(teamDBDao.rx()).thenReturn(teamRxDao);
        when(teamRxDao.insertOrReplace(any())).thenReturn(Observable.just(teamDB));
    }

    @Test
    public void shouldInsertShotIntoDBWhenAddingLike() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        //when
        repository.addLikedShot(Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        verify(shotDBDao).rx();
        verify(shotRxDao).insertOrReplace(any());
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldInsertUserIntoDBWhenAddingLike() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        //when
        repository.addLikedShot(Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        verify(userDBDao).rx();
        verify(userRxDao).insertOrReplace(any());
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldInsertTeamIntoDBWhenAddingLike() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        //when
        repository.addLikedShot(Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        verify(teamDBDao).rx();
        verify(teamRxDao).insertOrReplace(any());
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldQueryDBWhenGettingLikedShots() {
        //given
        final TestSubscriber<List<Shot>> subscriber = new TestSubscriber<>();
        when(shotDBDao.queryBuilder()).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.rx()).thenReturn(shotDBRxQuery);
        when(shotDBRxQuery.oneByOne()).thenReturn(Observable.empty());
        //when
        repository.getLikedShots().subscribe(subscriber);
        //then
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldDeleteFromDBWhenRemovingLikedShot() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        when(shotRxDao.deleteByKey(any())).thenReturn(Observable.empty());
        //when
        repository.removeLikedShot(Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        verify(shotRxDao).deleteByKey(any());
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnErrorWhenRemovingLikedShotFailed() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        final Throwable throwable = new Throwable();
        when(shotRxDao.deleteByKey(any())).thenReturn(Observable.error(throwable));
        //when
        repository.removeLikedShot(Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        subscriber.assertError(throwable);
    }

    @Test
    public void shouldBeNoErrorsWhenShotIsLiked() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        when(shotDBDao.queryBuilder()).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.where(any())).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.rx()).thenReturn(shotDBRxQuery);
        when(shotDBRxQuery.unique()).thenReturn(Observable.empty());
        //when
        repository.isShotLiked(Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        subscriber.assertNoErrors();
    }

    @Test
    public void shouldReturnErrorWhenShotIsNotLiked() {
        //given
        final TestSubscriber subscriber = new TestSubscriber();
        when(shotDBDao.queryBuilder()).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.where(any())).thenReturn(shotDBQueryBuilder);
        when(shotDBQueryBuilder.rx()).thenReturn(shotDBRxQuery);
        when(shotDBRxQuery.unique()).thenReturn(Observable.just(null));
        //when
        repository.isShotLiked(Statics.NOT_LIKED_SHOT).subscribe(subscriber);
        //then
        subscriber.assertError(Throwable.class);
    }
}