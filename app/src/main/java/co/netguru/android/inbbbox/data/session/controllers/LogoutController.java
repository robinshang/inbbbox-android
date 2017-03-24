package co.netguru.android.inbbbox.data.session.controllers;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.data.dribbbleuser.user.CurrentUserPrefsRepository;
import co.netguru.android.inbbbox.data.session.CookieCacheManager;
import co.netguru.android.inbbbox.data.session.TokenPrefsRepository;
import co.netguru.android.inbbbox.data.settings.SettingsPrefsRepository;
import rx.Completable;

@Singleton
public class LogoutController {

    private final TokenPrefsRepository tokenPrefsRepository;
    private final CurrentUserPrefsRepository currentUserPrefsRepository;
    private final SettingsPrefsRepository settingsPrefsRepository;
    private final CookieCacheManager cacheController;
    private final Context context;

    @Inject
    public LogoutController(TokenPrefsRepository tokenPrefsRepository,
                            CurrentUserPrefsRepository currentUserPrefsRepository,
                            SettingsPrefsRepository settingsPrefsRepository,
                            CookieCacheManager cacheController,
                            Context context) {
        this.tokenPrefsRepository = tokenPrefsRepository;
        this.currentUserPrefsRepository = currentUserPrefsRepository;
        this.settingsPrefsRepository = settingsPrefsRepository;
        this.cacheController = cacheController;
        this.context = context;
    }

    public Completable performLogout() {
        return Completable.merge(
                cacheController.clearCache(), tokenPrefsRepository.clear(),
                currentUserPrefsRepository.clear(), settingsPrefsRepository.clear())
                .doOnCompleted(() -> resetUserComponentAfterLogout(context));
    }

    private void resetUserComponentAfterLogout(Context context) {
        App.releaseUserComponent(context);
    }
}
