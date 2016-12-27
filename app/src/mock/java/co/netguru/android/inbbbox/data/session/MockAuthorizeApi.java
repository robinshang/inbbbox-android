package co.netguru.android.inbbbox.data.session;

import co.netguru.android.inbbbox.Constants;
import co.netguru.android.inbbbox.data.session.model.Token;
import retrofit2.http.Field;
import rx.Observable;

public class MockAuthorizeApi implements AuthorizeApi {

    private static final Token MOCKED_TOKEN = new Token("", "", "");

    static {
        MOCKED_TOKEN.setAccessToken("token");
        MOCKED_TOKEN.setTokenType("token_type");
        MOCKED_TOKEN.setScope("token_scope");
    }

    @Override
    public Observable<Token> getToken(@Field(Constants.OAUTH.CLIENT_ID_KEY) String clientId, @Field(Constants.OAUTH.CLIENT_SECRET_KEY) String clientSecret, @Field(Constants.OAUTH.CODE_KEY) String code) {
        return Observable.just(MOCKED_TOKEN);
    }
}