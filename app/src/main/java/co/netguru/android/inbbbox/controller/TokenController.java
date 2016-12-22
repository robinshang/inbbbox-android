package co.netguru.android.inbbbox.controller;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.api.AuthorizeApi;
import co.netguru.android.inbbbox.localrepository.TokenPrefsRepository;
import co.netguru.android.inbbbox.model.api.Token;
import co.netguru.android.inbbbox.common.utils.StringUtil;
import rx.Completable;
import rx.Observable;
import rx.Single;

@Singleton
public class TokenController {

    private final AuthorizeApi api;
    private final TokenPrefsRepository tokenPrefsRepository;

    @Inject
    TokenController(AuthorizeApi api, TokenPrefsRepository tokenPrefsRepository) {
        this.api = api;
        this.tokenPrefsRepository = tokenPrefsRepository;
    }

    /**
     * Request new token from api.
     * Side effect: token is saved to prefs
     */
    public Observable<Token> requestNewToken(String code) {
        return api.getToken(BuildConfig.DRIBBBLE_CLIENT_KEY,
                BuildConfig.DRIBBBLE_CLIENT_SECRET, code)
                .flatMap(token ->
                        tokenPrefsRepository.saveToken(token).andThen(Observable.just(token)));
    }


    public Single<Boolean> isTokenValid() {
        return Single.fromCallable(() -> {
            Token token = tokenPrefsRepository.getToken();
            return token != null && !StringUtil.isBlank(token.getAccessToken());
        });
    }

    public Completable saveToken(Token token) {
        return tokenPrefsRepository.saveToken(token);
    }
}
