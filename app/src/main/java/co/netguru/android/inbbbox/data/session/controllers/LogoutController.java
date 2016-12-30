package co.netguru.android.inbbbox.data.session.controllers;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.dribbbleuser.user.CurrentUserPrefsRepository;
import co.netguru.android.inbbbox.data.session.CookieCacheManager;
import co.netguru.android.inbbbox.data.session.TokenPrefsRepository;
import co.netguru.android.inbbbox.data.settings.SettingsPrefsRepository;
import rx.Completable;

public class LogoutController {

    private final TokenPrefsRepository tokenPrefsRepository;
    private final CurrentUserPrefsRepository currentUserPrefsRepository;
    private final SettingsPrefsRepository settingsPrefsRepository;
    private final CookieCacheManager cacheController;

    @Inject
    public LogoutController(TokenPrefsRepository tokenPrefsRepository,
                            CurrentUserPrefsRepository currentUserPrefsRepository,
                            SettingsPrefsRepository settingsPrefsRepository,
                            CookieCacheManager cacheController) {

        this.tokenPrefsRepository = tokenPrefsRepository;
        this.currentUserPrefsRepository = currentUserPrefsRepository;
        this.settingsPrefsRepository = settingsPrefsRepository;
        this.cacheController = cacheController;
    }

    public Completable performLogout() {
        return Completable.merge(
                cacheController.clearCache(),
                tokenPrefsRepository.clear(),
                currentUserPrefsRepository.clear(),
                settingsPrefsRepository.clear());
    }
}
