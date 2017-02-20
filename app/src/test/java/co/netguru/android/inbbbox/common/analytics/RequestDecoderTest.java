package co.netguru.android.inbbbox.common.analytics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.Constants;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;

@RunWith(MockitoJUnitRunner.class)
public class RequestDecoderTest {

    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String PATH_LIKE = "shots/testshot123/like";
    private static final String PATH_COMMENTS = "shots/testshot123/comments";
    private static final String PATH_FOLLOW = "users/testuser123/follow";
    private static final String PATH_ADD_TO_BUCKET = "buckets/testbucket123/shots";
    private HttpUrl.Builder urlBuilder;

    @Mock
    RequestBody mockRequestBody;

    @Before
    public void setUp() throws Exception {
        urlBuilder = new HttpUrl.Builder().host(Constants.API.DRIBBLE_BASE_URL);
    }

    @Test
    public void whenDecodeRequestWithPostMethodAndLikeUrl_thenReturnLikeEnum() throws Exception {

    }

    @Test
    public void decodeRequest_followRequest() throws Exception {

    }

    @Test
    public void decodeRequest_commentRequest() throws Exception {

    }

    @Test
    public void decodeRequest_addToBucketRequest() throws Exception {

    }

}