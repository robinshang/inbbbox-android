package co.netguru.android.inbbbox.localrepository;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import co.netguru.android.inbbbox.Statics;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GuestModeLikesRepositoryTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    SharedPreferences sharedPreferencesMock;

    @Mock
    SharedPreferences.Editor editorMock;

    private GuestModeLikesRepository guestModeLikesRepository;

    private Shot exampleShot = Statics.LIKED_SHOT;

    private Gson gson = new Gson();

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setUp() {
        guestModeLikesRepository = new GuestModeLikesRepository(sharedPreferencesMock, gson);
        when(sharedPreferencesMock.edit()).thenReturn(editorMock);
        when(sharedPreferencesMock.getString(anyString(), anyString())).thenReturn("");
        when(editorMock.putString(anyString(), anyString())).thenReturn(editorMock);
    }

    @Test
    public void whenAddLikeIsCalled_thenGetLikesFromPrefs() {
        TestSubscriber subscriber = new TestSubscriber();

        guestModeLikesRepository.addLikedShot(exampleShot).subscribe(subscriber);

        verify(sharedPreferencesMock, times(1)).getString(anyString(), eq(""));
    }

    @SuppressLint("CommitPrefEdits")
    @Test
    public void whenAddLikeIsCalled_thenAddNewShotToLikeListAndSaveList() {
        TestSubscriber subscriber = new TestSubscriber();

        guestModeLikesRepository.addLikedShot(exampleShot).subscribe(subscriber);

        verify(sharedPreferencesMock, times(1)).edit();
        verify(editorMock, atLeastOnce()).putString(anyString(), anyString());
        verify(editorMock, atLeastOnce()).apply();
    }

    @Test
    public void whenIsLikedCalled_thenGetLikesFromPrefs() {
        TestSubscriber subscriber = new TestSubscriber();

        guestModeLikesRepository.isShotLiked(exampleShot).subscribe(subscriber);

        verify(sharedPreferencesMock, times(1)).getString(anyString(), eq(""));
    }

    @SuppressLint("CommitPrefEdits")
    @Test
    public void whenIsLikedCalledAndShotIsNotLiked_thenReturnError() {
        TestSubscriber subscriber = new TestSubscriber();
        when(sharedPreferencesMock.getString(anyString(), anyString()))
                .thenReturn("");

        guestModeLikesRepository.isShotLiked(exampleShot).subscribe(subscriber);

        subscriber.assertNotCompleted();
    }

    @Test
    public void whenRemoveCalled_thenGetLikesFromPrefs() {
        TestSubscriber subscriber = new TestSubscriber();

        guestModeLikesRepository.removeLikedShot(exampleShot).subscribe(subscriber);

        verify(sharedPreferencesMock, times(1)).getString(anyString(), eq(""));
    }

    @SuppressLint("CommitPrefEdits")
    @Test
    public void whenRemoveCalled_thenRemoveShotToLikeListAndSaveList() {
        TestSubscriber subscriber = new TestSubscriber();

        guestModeLikesRepository.removeLikedShot(exampleShot).subscribe(subscriber);

        verify(sharedPreferencesMock, times(1)).edit();
        verify(editorMock, atLeastOnce()).putString(anyString(), anyString());
        verify(editorMock, atLeastOnce()).apply();
    }

    @Test
    public void whenGetShotsCalled_thenGetLikesFromPrefs() {
        TestSubscriber<List> subscriber = new TestSubscriber<>();

        guestModeLikesRepository.getLikedShots().subscribe(subscriber);

        verify(sharedPreferencesMock, times(1)).getString(anyString(), eq(""));
    }

}