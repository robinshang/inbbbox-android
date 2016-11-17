package co.netguru.android.inbbbox.api;

import android.content.Context;

import java.io.IOException;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controler.ErrorMessageController;
import co.netguru.android.inbbbox.controler.LogoutController;
import co.netguru.android.inbbbox.feature.login.LoginActivity;
import co.netguru.android.inbbbox.localrepository.TokenPrefsRepository;
import co.netguru.android.inbbbox.model.api.Token;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class RequestInterceptor implements Interceptor {

    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_TYPE_JSON = "application/json";
    private static final String HEADER_AUTHORIZATION = "AUTHORIZATION";
    private static final int UNAUTHORIZED = 401;

    private final Context context;
    private final LogoutController logoutController;
    private ErrorMessageController messageController;
    private final TokenPrefsRepository tokenPrefsRepository;

    @Inject
    public RequestInterceptor(Context context,
                              LogoutController logoutController,
                              ErrorMessageController messageController,
                              TokenPrefsRepository tokenPrefsRepository) {
        this.context = context;
        this.logoutController = logoutController;
        this.messageController = messageController;
        this.tokenPrefsRepository = tokenPrefsRepository;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request newRequest = getRequestWithToken(original, tokenPrefsRepository.getToken());

        return handleResponseErrors(chain.proceed(newRequest));
    }

    private Response handleResponseErrors(Response response) {
        Response result = response;
        if (!response.isSuccessful() && response.code() == UNAUTHORIZED) {
            performLogout(messageController.getMessage(response.code()));
        }
        return result;
    }

    private void performLogout(String message) {
        logoutController.performLogout()
                .doOnCompleted(() -> showLoginScreen(message));
    }

    private void showLoginScreen(String message) {
        Timber.d(message);
        LoginActivity.startActivity(context, message);
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