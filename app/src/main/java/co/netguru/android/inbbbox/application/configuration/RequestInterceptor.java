package co.netguru.android.inbbbox.application.configuration;

import java.io.IOException;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.utils.Constants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    private Token token;

    @Inject
    public RequestInterceptor(Token token) {

        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header(Constants.API.HEDAER_ACCEPT, Constants.API.HEADER_TYPE_JSON)
                .header(Constants.API.HEADER_AUTHORIZATION, generateOauthField())
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    private String generateOauthField() {
        return " " +
                token.getTokenType() +
                " " +
                token.getAccessToken();
    }
}
