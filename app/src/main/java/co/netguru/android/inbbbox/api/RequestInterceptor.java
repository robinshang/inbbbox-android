package co.netguru.android.inbbbox.api;

import java.io.IOException;

import javax.inject.Inject;

import co.netguru.android.inbbbox.localrepository.TokenPrefsRepository;
import co.netguru.android.inbbbox.model.api.Token;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_TYPE_JSON = "application/json";
    private static final String HEADER_AUTHORIZATION = "AUTHORIZATION";

    private final TokenPrefsRepository tokenPrefsRepository;

    @Inject
    public RequestInterceptor(TokenPrefsRepository tokenPrefsRepository) {
        this.tokenPrefsRepository = tokenPrefsRepository;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request newRequest = getRequestWithToken(original, tokenPrefsRepository.getToken());

        return chain.proceed(newRequest);
    }

    private Request getRequestWithToken(Request original, Token token) {
        Request.Builder builder = original.newBuilder()
                .header(HEADER_ACCEPT, HEADER_TYPE_JSON)
                .method(original.method(), original.body());
        if (token != null) {
            builder.header(HEADER_AUTHORIZATION, generateOauthField(token));
        }
        return builder.build();
    }

    private String generateOauthField(Token token) {
        return " " +
                token.getTokenType() +
                " " +
                token.getAccessToken();
    }
}