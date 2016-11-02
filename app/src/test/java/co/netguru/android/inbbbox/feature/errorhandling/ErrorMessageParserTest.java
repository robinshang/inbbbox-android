package co.netguru.android.inbbbox.feature.errorhandling;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.utils.Constants;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ErrorMessageParserTest {

    @Mock
    private Context contextMock;

    @Mock
    private Resources resourcesMock;

    @InjectMocks
    private ErrorMessageParser parser;

    private String invalidUserLabel = "test1";
    private String invalidOauthLabel = "test2";
    private String unrecognizedErrorLabel = "test3";

    @Before
    public void setUp(){
        when(contextMock.getResources()).thenReturn(resourcesMock);
        when(resourcesMock.getString(R.string.invalid_user_error)).thenReturn(invalidUserLabel);
        when(resourcesMock.getString(R.string.invalid_outh_uri)).thenReturn(invalidOauthLabel);
        when(resourcesMock.getString(R.string.undefined_api_error)).thenReturn(unrecognizedErrorLabel);
    }

    @Test
    public void whenPassUnrecognizedThrowable_thenShowTextFromThrowable() {
        String expectedText = "test";
        Throwable throwable = new Throwable(expectedText);

        String result = parser.getError(throwable);
        Assert.assertEquals(expectedText, result);
    }

    @Test
    public void whenPassInvalidOauthUriErrorType_thenGetInvalidOauthUriLabel() {
        String expected = contextMock.getResources().getString(R.string.invalid_user_error);

        String result = parser.getErrorLabel(ErrorType.INVALID_USER_INSTANCE);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void whenPassInvalidUserInstanceErrorType_thenGetInvalidInstanceLabel() {
        String expected = contextMock.getResources().getString(R.string.invalid_outh_uri);

        String result = parser.getErrorLabel(ErrorType.INVALID_OAURH_URL);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void whenPassUnknownErrorCode_thenReturnUnrecognizedErrorLabel(){
        String expected = unrecognizedErrorLabel;

        String result = parser.getError(Constants.UNDEFINED);

        Assert.assertEquals(expected, result);
    }

}