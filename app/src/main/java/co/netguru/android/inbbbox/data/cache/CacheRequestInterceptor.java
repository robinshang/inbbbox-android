package co.netguru.android.inbbbox.data.cache;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static co.netguru.android.inbbbox.data.cache.CacheStrategy.CACHE_CONTROL_NO_CACHE;
import static co.netguru.android.inbbbox.data.cache.CacheStrategy.HEADER_CACHE_CONTROL;

public class CacheRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String cacheControl = chain.request().header(CacheStrategy.HEADER_CACHE_CONTROL);
        Request request = generateCacheRequest(chain.request(), cacheControl);
        return chain.proceed(request);
    }

    private Request generateCacheRequest(Request originalRequest, String cacheControl) {
        if (cacheControl != null) {
            return originalRequest;
        } else {
            return getNoCacheRequest(originalRequest);
        }
    }

    private Request getNoCacheRequest(Request originalRequest) {
        return originalRequest.newBuilder()
                .header(HEADER_CACHE_CONTROL, CACHE_CONTROL_NO_CACHE)
                .build();
    }
}
