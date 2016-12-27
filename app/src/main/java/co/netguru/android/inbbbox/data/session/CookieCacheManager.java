package co.netguru.android.inbbbox.data.session;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import javax.inject.Inject;

import rx.Completable;
import timber.log.Timber;

public class CookieCacheManager {

    private final Context context;

    @Inject
    CookieCacheManager(Context context) {

        this.context = context;
    }

    public Completable clearCache() {
        return Completable.fromCallable(() -> {
            Timber.d("Cookies cache clearing");
            clearCacheFolder();
            return null;
        });
    }

    private void clearCacheFolder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            clearCookiesForNewApi();
        } else {
            clearCookiesOnOlderAPIs();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void clearCookiesForNewApi() {
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
        Timber.d("Cookies cache cleared (new api)");
    }

    @SuppressWarnings("deprecation")
    private void clearCookiesOnOlderAPIs() {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
        cookieSyncManager.startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
        cookieSyncManager.stopSync();
        cookieSyncManager.sync();
        Timber.d("Cookies cache cleared (old api)");
    }
}
