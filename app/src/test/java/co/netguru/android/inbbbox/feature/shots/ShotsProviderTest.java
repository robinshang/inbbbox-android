package co.netguru.android.inbbbox.feature.shots;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.data.api.ShotsApi;
import co.netguru.android.inbbbox.db.CacheEndpoint;
import co.netguru.android.testcommons.RxSyncTestRule;
import okhttp3.Cache;

import static org.junit.Assert.*;

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
    public CacheEndpoint cacheEndpointMock;

    @InjectMocks
    private ShotsProvider shotsProvider;

    @Test
    public void test() {

    }
}