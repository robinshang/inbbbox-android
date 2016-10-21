package co.netguru.android.inbbbox.feature.authentication;

import android.content.res.Resources;
import android.net.TrafficStats;
import android.net.Uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import co.netguru.android.testcommons.RxSyncTestRule;
import rx.observers.TestSubscriber;
import timber.log.Timber;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OauthUriProviderTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    Resources resourcesMock;

    @InjectMocks
    OauthUriProvider uriProvider;

    private String resourceString = "TEST";

    @Before
    public void setUp() {
        when(resourcesMock.getString(anyInt())).thenReturn(resourceString);
    }

    @Test
    public void whenUriRequested_thenGenerateUriFromConstants() {
        String expected = generateExpectedUri();
        TestSubscriber<String> testSubscriber = new TestSubscriber();

        uriProvider.getOauthAuthorizeUriString().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        String uriString = testSubscriber.getOnNextEvents().get(0);

        Assert.assertEquals(uriString.startsWith(expected), true);
    }

    private String generateExpectedUri() {
        return "https://dribbble.com/oauth/authorize?client_id=";
    }


}