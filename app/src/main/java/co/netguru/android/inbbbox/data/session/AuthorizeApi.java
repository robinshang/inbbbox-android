package co.netguru.android.inbbbox.data.session;

import co.netguru.android.inbbbox.Constants.OAUTH;
import co.netguru.android.inbbbox.data.session.model.Token;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

@FunctionalInterface
public interface AuthorizeApi {

    @FormUrlEncoded
    @POST(OAUTH.OAUTH_TOKEN_ENDPOINT)
    Observable<Token> getToken(
            @Field(OAUTH.CLIENT_ID_KEY) String clientId,
            @Field(OAUTH.CLIENT_SECRET_KEY) String clientSecret,
            @Field(OAUTH.CODE_KEY) String code);
}
