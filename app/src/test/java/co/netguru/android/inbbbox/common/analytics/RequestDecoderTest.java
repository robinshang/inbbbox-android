package co.netguru.android.inbbbox.common.analytics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.netguru.android.inbbbox.Constants;
import okhttp3.Request;
import okhttp3.RequestBody;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RequestDecoderTest {

    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String PATH_LIKE = "shots/testshot123/like";
    private static final String PATH_COMMENTS = "shots/testshot123/comments";
    private static final String PATH_FOLLOW = "users/testuser123/follow";
    private static final String PATH_ADD_TO_BUCKET = "buckets/testbucket123/shots";

    @Mock
    RequestBody mockRequestBody;

    @Test
    public void whenDecodeRequestWithPostMethodAndLikeUrl_thenReturnLikeEnum() throws Exception {
        Request request = new Request.Builder()
                .url(Constants.API.DRIBBLE_BASE_URL + PATH_LIKE)
                .method(METHOD_POST, mockRequestBody)
                .build();

        RequestDecoder result = RequestDecoder.decodeRequest(request);

        assertEquals(RequestDecoder.LIKE, result);
    }

    @Test
    public void whenDecodeRequestWithPutMethodAndAddToBucketUrl_thenReturnBucketEnum()
            throws Exception {
        Request request = new Request.Builder()
                .url(Constants.API.DRIBBLE_BASE_URL + PATH_ADD_TO_BUCKET)
                .method(METHOD_PUT, mockRequestBody)
                .build();

        RequestDecoder result = RequestDecoder.decodeRequest(request);

        assertEquals(RequestDecoder.ADD_SHOT_TO_BUCKET, result);
    }

    @Test
    public void whenDecodeRequestWithPutMethodAndFollowUrl_thenReturnFollowEnum() throws Exception {
        Request request = new Request.Builder()
                .url(Constants.API.DRIBBLE_BASE_URL + PATH_FOLLOW)
                .method(METHOD_PUT, mockRequestBody)
                .build();

        RequestDecoder result = RequestDecoder.decodeRequest(request);

        assertEquals(RequestDecoder.FOLLOW, result);
    }

    @Test
    public void whenDecodeRequestWithPostMethodAndCommentUrl_thenReturnCommentEnum()
            throws Exception {
        Request request = new Request.Builder()
                .url(Constants.API.DRIBBLE_BASE_URL + PATH_COMMENTS)
                .method(METHOD_POST, mockRequestBody)
                .build();

        RequestDecoder result = RequestDecoder.decodeRequest(request);

        assertEquals(RequestDecoder.COMMENT, result);
    }

    @Test
    public void whenDecodeRequestWithNonMatchingMethodAndPath_thenReturnOtherEnum()
            throws Exception {
        Request request = new Request.Builder()
                .url(Constants.API.DRIBBLE_BASE_URL + PATH_LIKE)
                .method(METHOD_PUT, mockRequestBody)
                .build();

        RequestDecoder result = RequestDecoder.decodeRequest(request);

        assertEquals(RequestDecoder.OTHER, result);
    }
}