package co.netguru.android.inbbbox.api;

import java.io.IOException;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.db.Storage;
import co.netguru.android.inbbbox.utils.Constants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    private Token token;
    private Storage storage;

    @Inject
    public RequestInterceptor(Storage storage) {

        this.storage = storage;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        loadToken();
        Request original = chain.request();
        Request.Builder requestBuilder = getRequestBuilderWithToken(original);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    private Request.Builder getRequestBuilderWithToken(Request original) {
        Request.Builder builder = original.newBuilder()
                .header(Constants.API.HEADER_ACCEPT, Constants.API.HEADER_TYPE_JSON)
                .method(original.method(), original.body());
        if (token != null) {
            builder.header(Constants.API.HEADER_AUTHORIZATION, generateOauthField());
        }
        return builder;
    }

    private void loadToken() {
        if (token == null) {
            loadTokenFromStorage();
        }
    }

    private void loadTokenFromStorage() {
        try {
            token = storage.get(Constants.Db.TOKEN_KEY, Token.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateOauthField() {
        return " " +
                token.getTokenType() +
                " " +
                token.getAccessToken();
    }

}
