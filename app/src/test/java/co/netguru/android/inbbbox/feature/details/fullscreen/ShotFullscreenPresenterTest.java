package co.netguru.android.inbbbox.feature.details.fullscreen;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import co.netguru.android.inbbbox.model.ui.Shot;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)

public class ShotFullscreenPresenterTest {

    @InjectMocks
    ShotFullScreenPresenter shotFullScreenPresenter;

    @Mock
    ShotFullscreenContract.View viewMock;

    @Mock
    Shot shotMock;

    @Before
    public void setUp() {
        shotFullScreenPresenter.attachView(viewMock);
    }

    @Test
    public void whenViewCreated_thenShowShotFullscreen() {
        shotFullScreenPresenter.onViewCreated(shotMock, Collections.emptyList());

        verify(viewMock, times(1)).previewShot(any(Shot.class), any(List.class));
    }
}
