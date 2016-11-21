package co.netguru.android.inbbbox.feature.details;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.ShotDetailsController;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.testcommons.RxSyncTestRule;

@RunWith(MockitoJUnitRunner.class)
public class ShotDetailsPresenterTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    Shot shotMock;

    @Mock
    ShotDetailsController shotDetailsControllerMock;

    @Mock
    ErrorMessageController errorMessageControllerMock;

    @InjectMocks
    ShotDetailsPresenter shotDetailsPresenter;

    @Test
    public void initialTest() {
        Assert.assertEquals(true, true);
    }

}