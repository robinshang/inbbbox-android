package co.netguru.android.inbbbox.feature.authentication;

import javax.inject.Inject;

import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.data.api.AuthorizeApi;
import co.netguru.android.inbbbox.data.local.TokenPrefsController;
import co.netguru.android.inbbbox.models.Token;
import co.netguru.android.inbbbox.utils.StringUtils;
import rx.Observable;

public class TokenProvider {

    private final AuthorizeApi api;
    private final TokenPrefsController tokenPrefsController;

    @Inject
    TokenProvider(AuthorizeApi api, TokenPrefsController tokenPrefsController) {
        this.api = api;
        this.tokenPrefsController = tokenPrefsController;
    }

    /**
     * Request new token from api.
     * Side effect: token is saved to prefs
     */
    public Observable<Token> requestNewToken(String code) {
        return api.getToken(BuildConfig.DRIBBBLE_CLIENT_KEY,
                BuildConfig.DRIBBBLE_CLIENT_SECRET, code)
                .flatMap(token ->
                        tokenPrefsController.saveToken(token).andThen(Observable.just(token)));
    }


    public Observable<Boolean> isTokenValid() {
        return Observable.fromCallable(() -> {
            Token token = tokenPrefsController.getToken();
            return token != null && StringUtils.isBlank(token.getAccessToken());
        });
    }
}
