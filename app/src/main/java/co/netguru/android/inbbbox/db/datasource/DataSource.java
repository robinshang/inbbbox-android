package co.netguru.android.inbbbox.db.datasource;

import java.io.Serializable;

import rx.Observable;
import rx.Single;

public interface DataSource<T extends Serializable> {

    Observable<Boolean> save(T object);

    Observable<T> get();
}
