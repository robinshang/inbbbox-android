package co.netguru.android.inbbbox.data.session;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.session.controllers.LogoutController;
import co.netguru.android.inbbbox.data.session.model.Token;
import co.netguru.android.inbbbox.event.RxBus;
import co.netguru.android.inbbbox.event.events.CriticalLogoutEvent;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class RequestInterceptor implements Interceptor {

    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_TYPE_JSON = "application/json";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    private final LogoutController logoutController;
    private final ErrorController errorController;
    private final TokenPrefsRepository tokenPrefsRepository;
    private final Context context;
    private final RxBus rxBus;

    @Inject
    public RequestInterceptor(LogoutController logoutController,
                              ErrorController errorController,
                              TokenPrefsRepository tokenPrefsRepository,
                              RxBus rxBus,
                              Context context) {
        this.logoutController = logoutController;
        this.errorController = errorController;
        this.tokenPrefsRepository = tokenPrefsRepository;
        this.rxBus = rxBus;
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request newRequest = getRequestWithToken(original, tokenPrefsRepository.getToken());

        checkInternetConnection();
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

    private void checkInternetConnection() throws IOException {
        final ConnectivityManager conMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected()) {
            throw  new IOException(context.getString(R.string.error_no_internet));
        }
    }
}