package co.netguru.android.inbbbox.common.analytics;

import java.io.IOException;

import co.netguru.android.inbbbox.Constants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class AnalyticsInterceptor implements Interceptor {

    private static final String HEADER_REQUESTS_REMAINING = "X-RateLimit-Remaining";
    private static final String DEFAULT_VALUE = "60";

    private final AnalyticsEventLogger eventLogger;

    AnalyticsInterceptor(AnalyticsEventLogger eventLogger) {
        this.eventLogger = eventLogger;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (request.url().toString().contains(Constants.API.DRIBBLE_BASE_URL)) {
            logEvents(request, response);
        }
        return response;
    }

    private void logEvents(Request request, Response response) {
        try {
            int requestsRemaining = Integer.parseInt(
                    response.header(HEADER_REQUESTS_REMAINING, DEFAULT_VALUE));
            logUserEvents(request, requestsRemaining);
        } catch (NumberFormatException e) {
            Timber.e(e, "Could not parse remaining request count, %s", e.getMessage());
        }
    }

    private void logUserEvents(Request request, int requestsRemaining) {
        switch (RequestDecoder.decodeRequest(request)) {
            case ADD_SHOT_TO_BUCKET:
                eventLogger.logEventApiAddToBucket(requestsRemaining);
                break;
            case LIKE:
                eventLogger.logEventApiLike(requestsRemaining);
                break;
            case FOLLOW:
                eventLogger.logEventApiFollow(requestsRemaining);
                break;
            case COMMENT:
                eventLogger.logEventApiComment(requestsRemaining);
                break;
            default:
                eventLogger.logEventApiOther(requestsRemaining);
        }
        eventLogger.logEventApiRequestsRemaining(requestsRemaining);
    }
}
