package co.netguru.android.inbbbox.feature.shots;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.meta.When;

import co.netguru.android.inbbbox.data.api.ShotsApi;
import co.netguru.android.inbbbox.data.models.Settings;
import co.netguru.android.inbbbox.data.models.StreamSourceState;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import co.netguru.android.testcommons.RxSyncTestRule;
import okhttp3.Cache;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShotsProviderTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    public ShotsMapper shotsMapperMock;

    @Mock
    public ShotsApi shotsApiMock;

    @Mock
    public ShotsRequestFactory shotsRequestFactoryMock;

    @Mock
    public DataSource<Settings> cacheEndpointMock;

    @Mock
    private Settings settingsMock;

    @Mock
    private StreamSourceState streamSourceState;

    @InjectMocks
    private ShotsProvider shotsProvider;

    @Before
    public void setUp() {
        when(settingsMock.getStreamSourceState()).thenReturn(streamSourceState);
    }

    @Test
    public void whenFollowingSourceEnabled_thenReturnFollowingItemsFromFollowingEndpoint() {

        shotsProvider.getShots();
    }
}