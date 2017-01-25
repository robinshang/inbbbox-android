package co.netguru.android.inbbbox.feature.like;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.like.controllers.LikeShotController;
import co.netguru.android.testcommons.RxSyncTestRule;

@RunWith(MockitoJUnitRunner.class)
public class LikesPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public ErrorController errorControllerMock;

    @Mock
    public LikeShotController likeShotControllerMock;

    @Mock
    public LikesViewContract.View viewMock;

    @InjectMocks
    public LikesPresenter likesPresenter;

    @Before
    public void setup() {
        likesPresenter.attachView(viewMock);
    }

    @Ignore
    @Test
    public void when_then() {

        likesPresenter.getLikesFromServer();
    }


    @After
    public void tearDown() {
        likesPresenter.detachView(false);
    }
}