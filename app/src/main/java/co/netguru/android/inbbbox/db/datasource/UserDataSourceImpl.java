package co.netguru.android.inbbbox.db.datasource;

import co.netguru.android.inbbbox.data.models.User;
import co.netguru.android.inbbbox.db.Storage;
import co.netguru.android.inbbbox.utils.Constants;
import rx.Observable;
import timber.log.Timber;

public class UserDataSourceImpl implements DataSource<User> {

    private final Storage storage;

    public UserDataSourceImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Observable<Boolean> save(User user) {
        return Observable.just(user)
                .map(this::saveUserToDatabase);
    }

    @Override
    public Observable<User> get() {
        return Observable.just(Constants.Db.CURRENT_USER_KEY)
                .flatMap(this::getUserFromDatabase);
    }

    private boolean saveUserToDatabase(User user) {
        try {
            storage.put(Constants.Db.CURRENT_USER_KEY, user);

            return true;
        } catch (Exception e) {
            Timber.i(e, "Error while saving user to database");
            return false;
        }
    }

    private Observable<User> getUserFromDatabase(String key) {
        Observable<User> resultObservable = null;
        try {
            resultObservable = Observable.just(storage.get(key, User.class));
        } catch (Exception e) {
            Timber.i(e, "Error while getting user from database");
            resultObservable = Observable.empty();
        }

        return resultObservable;
    }
}
