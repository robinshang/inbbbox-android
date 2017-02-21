package co.netguru.android.inbbbox.common.analytics;

import java.io.IOException;

import co.netguru.android.inbbbox.Constants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class AnalyticsInterceptor implements Interceptor {

    private static final String HEADER_REQUESTS_REMAINING = "X-RateLimit-Remaining";
    private static final String DEFAULT_VALUE = "0";

    private final AnalyticsEventLogger eventLogger;

    AnalyticsInterceptor(AnalyticsEventLogger eventLogger) {
        this.eventLogger = eventLogger;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (request.url().toString().contains(Constants.API.DRIBBLE_BASE_URL)) {
            logUserEvents(request);
            logRequestsRemaining(response.header(HEADER_REQUESTS_REMAINING, DEFAULT_VALUE));
        }
        return response;
    }

    private void logUserEvents(Request request) {
        switch (RequestDecoder.decodeRequest(request)) {
            case ADD_SHOT_TO_BUCKET:
                eventLogger.logEventApiAddToBucket();
                break;
            case LIKE:
                eventLogger.logEventApiLike();
                break;
            case FOLLOW:
                eventLogger.logEventApiFollow();
                break;
            case COMMENT:
                eventLogger.logEventApiComment();
                break;
            default:
        }
    }

    private void logRequestsRemaining(String headerValue) {
        try {
            eventLogger.logEventApiRequestsRemaining(Integer.parseInt(headerValue));
        } catch (NumberFormatException e) {
            Timber.e(e, "Could not parse remaining request count, %s", e.getMessage());
        }
    }
}
