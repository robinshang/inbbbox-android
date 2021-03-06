package co.netguru.android.inbbbox.data.session.controllers;

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

import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.data.session.model.Token;
import co.netguru.android.testcommons.RxSyncTestRule;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenParametersControllerTest {

    @Rule
    public TestRule rule = new RxSyncTestRule();

    @Mock
    Resources resourcesMock;

    @InjectMocks
    TokenParametersController tokenParametersController;

    private String resourceString = "TEST";

    @Before
    public void setUp() {
        when(resourcesMock.getString(anyInt())).thenReturn(resourceString);
    }

    @Test
    public void whenGetUserPublicSubscribed_thenReturnDefaultToken() {
        TestSubscriber<Token> testSubscriber = new TestSubscriber<>();

        tokenParametersController.getUserGuestToken().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Token token = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(BuildConfig.DRIBBBLE_CLIENT_TOKEN, token.getAccessToken());
    }

    @Test
    public void whenGetSignUpUrlSubscribed_thenReturnSignedUrlWithOauthBaseUrlAndSignIpEndpoint() {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();

        tokenParametersController.getSignUpUrl().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        String url = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(true, url.contains(Constants.OAUTH.BASE_URL));
        Assert.assertEquals(true, url.contains(Constants.OAUTH.SIGN_UP_ENDPOINT));
    }


    private String generateExpectedUrl() {
        return "https://dribbble.com/oauth/authorize?client_id=";
    }
}