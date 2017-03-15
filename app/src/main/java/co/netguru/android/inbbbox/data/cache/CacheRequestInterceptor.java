package co.netguru.android.inbbbox.data.cache;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class CacheRequestInterceptor implements Interceptor {

    private static final int MEDIUM_CACHE_TIME = 60 * 60 * 24; // 24 hours
    private static final int LONG_CACHE_TIME = 60 * 60 * 72; // 24 hours
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String CACHE_CONTROL_NO_CACHE = "no-cache";
    private static final String CACHE_CONTROL_CACHE = "public, max-age=";

    @Override
    public Response intercept(Chain chain) throws IOException {
        String cacheStrategy = chain.request().header(CacheStrategy.HEADER_CACHE_STRATEGY);

        Request request = generateCacheRequest(chain.request(), cacheStrategy);
        Response originalResponse = chain.proceed(request);
        return generateCacheResponse(originalResponse);
    }

    private Request generateCacheRequest(Request originalRequest, String cacheStrategy) {
        Timber.d("Request wants to use cache strategy: " + cacheStrategy);

        if(cacheStrategy != null) {
            switch (cacheStrategy) {
                case CacheStrategy.CACHE_STRATEGY_CACHE_LONG:
                    return getLongCacheRequest(originalRequest);
                case CacheStrategy.CACHE_STRATEGY_CACHE_MEDIUM:
                    return getMediumCacheRequest(originalRequest);
                default:
                    return getNoCacheRequest(originalRequest);
            }
        } else {
            return getNoCacheRequest(originalRequest);
        }
    }

    private Response generateCacheResponse(Response originalResponse) {
        return originalResponse.newBuilder()
                .header(HEADER_CACHE_CONTROL, CACHE_CONTROL_CACHE + LONG_CACHE_TIME)
                .build();
    }

    private Request getLongCacheRequest(Request originalRequest) {
        return originalRequest.newBuilder()
                .header(HEADER_CACHE_CONTROL, CACHE_CONTROL_CACHE + LONG_CACHE_TIME)
                .build();
    }

    private Request getMediumCacheRequest(Request originalRequest) {
        return originalRequest.newBuilder()
                .header(HEADER_CACHE_CONTROL, CACHE_CONTROL_CACHE + MEDIUM_CACHE_TIME)
                .build();
    }

    private Request getNoCacheRequest(Request originalRequest) {
        return originalRequest.newBuilder()
                .header(HEADER_CACHE_CONTROL, CACHE_CONTROL_NO_CACHE)
                .build();
    }
}
