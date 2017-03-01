package co.netguru.android.inbbbox.data.session;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.session.model.Token;
import rx.Completable;

@Singleton
public class TokenPrefsRepository {

    private static final String TOKEN_ACCESS_TOKEN_KEY = "token_access_token";
    private static final String TOKEN_SCOPE_KEY = "token_scope";
    private static final String TOKEN_TYPE_TOKEN_KEY = "token_type_token";

    private final SharedPreferences sharedPreferences;

    @Inject
    public TokenPrefsRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Completable saveToken(Token token) {
        return Completable.fromAction(() ->
            sharedPreferences.edit()
                    .putString(TOKEN_ACCESS_TOKEN_KEY, token.getAccessToken())
                    .putString(TOKEN_TYPE_TOKEN_KEY, token.getTokenType())
                    .putString(TOKEN_SCOPE_KEY, token.getScope())
                    .apply()
        );
    }

    public Token getToken() {
        return new Token(
                sharedPreferences.getString(TOKEN_ACCESS_TOKEN_KEY, null),
                sharedPreferences.getString(TOKEN_TYPE_TOKEN_KEY, null),
                sharedPreferences.getString(TOKEN_SCOPE_KEY, null));
    }

    public Completable clear() {
        return Completable.fromAction(() -> sharedPreferences.edit().clear().apply());
    }
}
