package co.netguru.android.inbbbox.data;

import java.util.ArrayList;
import java.util.List;

public class Cache<T extends Cacheable> {
    private List<T> cacheables;

    public Cache() {
        this.cacheables = new ArrayList<>();
    }

    public T get(long id) {
        for (T cacheable : this.cacheables)
            if (cacheable.getId() == id)
                return cacheable;
        return null;
    }

    public void add(T cacheable) {
        cacheables.add(cacheable);
    }
}
