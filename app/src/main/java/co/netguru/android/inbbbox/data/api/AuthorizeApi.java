package co.netguru.android.inbbbox.data.api;

import co.netguru.android.inbbbox.models.Token;
import co.netguru.android.inbbbox.utils.Constants.OAUTH;
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
