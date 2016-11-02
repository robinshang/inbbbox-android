package co.netguru.android.inbbbox.feature.authentication;

import javax.inject.Inject;

import co.netguru.android.inbbbox.BuildConfig;
import co.netguru.android.inbbbox.api.AuthorizeApi;
import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.db.datasource.DataSource;
import rx.Observable;

public class TokenProvider {

    private final AuthorizeApi api;
    private final DataSource<Token> dataSource;

    @Inject
    TokenProvider(AuthorizeApi api, DataSource<Token> dataSource) {
        this.api = api;
        this.dataSource = dataSource;
    }

    public Observable<Boolean> getToken(String code) {
        return api.getToken(BuildConfig.DRIBBBLE_CLIENT_KEY,
                BuildConfig.DRIBBBLE_CLIENT_SECRET, code)
                .flatMap(this::saveTokenToStorage);
    }

    private Observable<Boolean> saveTokenToStorage(Token tokenResponse) {
        return dataSource.save(tokenResponse);
    }

    public Observable<Boolean> isTokenValid() {
        return dataSource.get()
                .flatMap(this::checkToken);
    }

    private Observable<Boolean> checkToken(Token token) {
        return Observable.just(token != null && token.getAccessToken() != null);
    }
}
