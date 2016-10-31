package co.netguru.android.inbbbox.feature.authentication;

import android.content.res.Resources;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.testcommons.RxSyncTestRule;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OauthUrlProviderTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    Resources resourcesMock;

    @InjectMocks
    OauthUrlProvider urlProvider;

    private String resourceString = "TEST";

    @Before
    public void setUp() {
        when(resourcesMock.getString(anyInt())).thenReturn(resourceString);
    }

    @Test
    public void whenUrlRequested_thenGenerateUrlFromConstants() {
        String expected = generateExpectedUrl();
        TestSubscriber<String> testSubscriber = new TestSubscriber();

        urlProvider.getOauthAuthorizeUrlString().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        String urlString = testSubscriber.getOnNextEvents().get(0);

        Assert.assertEquals(urlString.startsWith(expected), true);
    }

    private String generateExpectedUrl() {
        return "https://dribbble.com/oauth/authorize?client_id=";
    }


}