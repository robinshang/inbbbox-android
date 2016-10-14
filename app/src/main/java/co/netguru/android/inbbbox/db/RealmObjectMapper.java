package co.netguru.android.inbbbox.db;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.data.realm.RealmToken;
import io.realm.Realm;
import io.realm.RealmObject;

public class RealmObjectMapper {

    private Realm realm;

    @Inject
    RealmObjectMapper(){
        realm = Realm.getDefaultInstance();
    }

    public RealmToken toRealmObject(Token token) {
        RealmToken realmToken = realm.createObject(RealmToken.class);
        realmToken.setAccessToken(token.getAccessToken());
        realmToken.setScope(token.getScope());
        realmToken.setTokenType(token.getTokenType());
        return null;
    }
}
