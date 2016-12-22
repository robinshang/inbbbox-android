package co.netguru.android.inbbbox.controller;

import javax.inject.Inject;

import co.netguru.android.inbbbox.localrepository.SettingsPrefsRepository;
import co.netguru.android.inbbbox.localrepository.TokenPrefsRepository;
import co.netguru.android.inbbbox.localrepository.UserPrefsRepository;
import rx.Completable;

public class LogoutController {

    private final TokenPrefsRepository tokenPrefsRepository;
    private final UserPrefsRepository userPrefsRepository;
    private final SettingsPrefsRepository settingsPrefsRepository;
    private final CookieCacheManager cacheController;

    @Inject
    public LogoutController(TokenPrefsRepository tokenPrefsRepository,
                            UserPrefsRepository userPrefsRepository,
                            SettingsPrefsRepository settingsPrefsRepository,
                            CookieCacheManager cacheController) {

        this.tokenPrefsRepository = tokenPrefsRepository;
        this.userPrefsRepository = userPrefsRepository;
        this.settingsPrefsRepository = settingsPrefsRepository;
        this.cacheController = cacheController;
    }

    public Completable performLogout() {
        return Completable.merge(
                cacheController.clearCache(),
                tokenPrefsRepository.clear(),
                userPrefsRepository.clear(),
                settingsPrefsRepository.clear());
    }
}
