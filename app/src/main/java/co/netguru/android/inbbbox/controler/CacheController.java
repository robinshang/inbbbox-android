package co.netguru.android.inbbbox.controler;

import android.content.Context;

import java.io.File;
import java.util.Date;

import javax.inject.Inject;

import rx.Completable;
import timber.log.Timber;

public class CacheController {

    private Context context;

    @Inject
    CacheController(Context context) {

        this.context = context;
    }

    public Completable clearCache() {
        return Completable.fromCallable(this::clearCache);
    }

    private void clearCacheFolder(final File dir) {

        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    deleteDirectories(child);
                    deleteSubDirectories(child);
                }
            } catch (Exception e) {
                Timber.e(e, "Error during cache clearing");
            }
        }
    }

    private void deleteSubDirectories(File child) throws Exception {
        if (child.lastModified() < new Date().getTime()) {
            child.delete();
        }
    }

    private void deleteDirectories(File child) throws Exception {
        Timber.d("Clearing cache");
        if (child.isDirectory()) {
            clearCacheFolder(child);
        }
    }
}
