package co.netguru.android.inbbbox.controller.authentication;

import android.content.res.Resources;
import android.support.v4.util.Pair;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import co.netguru.android.inbbbox.controler.OauthUrlController;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OauthUrlControllerTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    Resources resourcesMock;

    @InjectMocks
    OauthUrlController urlProvider;

    private String resourceString = "TEST";

    @Before
    public void setUp() {
        when(resourcesMock.getString(anyInt())).thenReturn(resourceString);
    }

    @Test
    public void whenUrlRequested_thenGenerateUrlFromConstants() {
        String expected = generateExpectedUrl();
        TestSubscriber<Pair<String, UUID>> testSubscriber = new TestSubscriber<>();

        urlProvider.getOauthAuthorizeUrlAndUuidPair().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        String urlString = testSubscriber.getOnNextEvents().get(0).first;
        UUID uuid = testSubscriber.getOnNextEvents().get(0).second;

        Assert.assertTrue(urlString.startsWith(expected));
        Assert.assertTrue(urlString.endsWith(uuid.toString()));
    }

    private String generateExpectedUrl() {
        return "https://dribbble.com/oauth/authorize?client_id=";
    }


}