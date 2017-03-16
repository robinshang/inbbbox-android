package co.netguru.android.inbbbox.data.cache;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

import static co.netguru.android.inbbbox.data.cache.CacheStrategy.CACHE_CONTROL_MAX_AGE;
import static co.netguru.android.inbbbox.data.cache.CacheStrategy.CACHE_CONTROL_NO_CACHE;
import static co.netguru.android.inbbbox.data.cache.CacheStrategy.HEADER_CACHE_CONTROL;
import static co.netguru.android.inbbbox.data.cache.CacheStrategy.LONG_CACHE_TIME;

public class CacheResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String cacheStrategy = chain.request().header(CacheStrategy.HEADER_CACHE_CONTROL);

        Response originalResponse = chain.proceed(chain.request());
        return generateCacheResponse(originalResponse, cacheStrategy);
    }

    private Response generateCacheResponse(Response originalResponse, String cacheStrategy) {
        if (cacheStrategy == null) {
            return originalResponse.newBuilder()
                    .header(HEADER_CACHE_CONTROL, CACHE_CONTROL_NO_CACHE)
                    .build();
        } else {
            return originalResponse.newBuilder()
                    .header(HEADER_CACHE_CONTROL, CACHE_CONTROL_MAX_AGE + LONG_CACHE_TIME)
                    .build();
        }
    }
}
