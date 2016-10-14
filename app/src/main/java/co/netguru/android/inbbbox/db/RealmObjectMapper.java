package co.netguru.android.inbbbox.db;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.data.realm.RealmToken;
import co.netguru.android.inbbbox.utils.Constants;
import io.realm.Realm;
import io.realm.RealmObject;

public class RealmObjectMapper {

    @Inject
    RealmObjectMapper() {
    }

    public RealmToken toRealmObject(Token token, Realm realm) {
        RealmToken realmToken = realm.createObject(RealmToken.class);
        realmToken.setKey(Constants.Realm.TOKEN_KEY);
        realmToken.setAccessToken(token.getAccessToken());
        realmToken.setScope(token.getScope());
        realmToken.setTokenType(token.getTokenType());
        return realmToken;
    }
}
