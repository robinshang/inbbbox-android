package co.netguru.android.inbbbox.db.datasource;

import co.netguru.android.inbbbox.data.models.Token;
import co.netguru.android.inbbbox.db.Storage;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;
import timber.log.Timber;

public class TokenDataSourceImpl implements DataSource<Token> {

    private final Storage storage;

    public TokenDataSourceImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Observable<Boolean> save(Token token) {
        return Observable.just(token)
                .map(this::saveTokenToDatabase);
    }

    @Override
    public Observable<Token> get() {
        return Observable.just(Constants.Db.TOKEN_KEY)
                .flatMap(this::getTokenFromDatabase);
    }

    private boolean saveTokenToDatabase(Token token) {
        boolean result;
        try {
            storage.put(Constants.Db.TOKEN_KEY, token);

            result = true;
        } catch (Exception e) {
            Timber.i(e, "Error while saving token to database");

            result = false;
        }
        return result;
    }

    private Observable<Token> getTokenFromDatabase(String key) {
        try {
            return Observable.just(storage.get(key, Token.class));
        } catch (Exception e) {
            Timber.i(e, "Error while getting token from database");
        }

        return Observable.empty();
    }
}
