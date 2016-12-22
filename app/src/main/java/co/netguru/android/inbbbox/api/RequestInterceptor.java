package co.netguru.android.inbbbox.api;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;

import co.netguru.android.inbbbox.controller.ErrorController;
import co.netguru.android.inbbbox.controller.LogoutController;
import co.netguru.android.inbbbox.event.events.CriticalLogoutEvent;
import co.netguru.android.inbbbox.event.RxBus;
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

    private final LogoutController logoutController;
    private final ErrorController errorController;
    private final TokenPrefsRepository tokenPrefsRepository;
    private final RxBus rxBus;

    @Inject
    public RequestInterceptor(LogoutController logoutController,
                              ErrorController errorController,
                              TokenPrefsRepository tokenPrefsRepository,
                              RxBus rxBus) {
        this.logoutController = logoutController;
        this.errorController = errorController;
        this.tokenPrefsRepository = tokenPrefsRepository;
        this.rxBus = rxBus;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request newRequest = getRequestWithToken(original, tokenPrefsRepository.getToken());

        return handleResponseErrors(chain.proceed(newRequest));
    }

    private Response handleResponseErrors(Response response) {
        if (!response.isSuccessful() && response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            performLogout(errorController.getMessageBasedOnErrorCode(response.code()));
        }
        return response;
    }

    private void performLogout(String message) {
        logoutController.performLogout()
                .subscribe(() -> sendCriticalLogoutEvent(message),
                        throwable -> Timber.e(throwable, "critical logout error"));
    }

    private void sendCriticalLogoutEvent(String message) {
        Timber.d(message);
        rxBus.send(new CriticalLogoutEvent(message));
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