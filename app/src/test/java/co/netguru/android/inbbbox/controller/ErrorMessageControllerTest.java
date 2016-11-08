package co.netguru.android.inbbbox.controller;

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
import co.netguru.android.inbbbox.controler.ErrorMessageController;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ErrorMessageControllerTest {

    @Mock
    private Context contextMock;

    @Mock
    private Resources resourcesMock;

    @InjectMocks
    private ErrorMessageController parser;

    private String invalidUserLabel = "test1";
    private String invalidOauthLabel = "test2";
    private String unrecognizedErrorLabel = "test3";

    @Before
    public void setUp() {
        when(contextMock.getResources()).thenReturn(resourcesMock);
        when(resourcesMock.getString(R.string.invalid_user_error)).thenReturn(invalidUserLabel);
        when(resourcesMock.getString(R.string.invalid_outh_url)).thenReturn(invalidOauthLabel);
        when(resourcesMock.getString(R.string.undefined_api_error)).thenReturn(unrecognizedErrorLabel);
    }

    @Test
    public void whenPassUnrecognizedThrowable_thenShowTextFromThrowable() {
        String expectedText = "test";
        Throwable throwable = new Throwable(expectedText);

        String result = parser.getError(throwable);
        Assert.assertEquals(expectedText, result);
    }
}