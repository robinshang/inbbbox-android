package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;

import co.netguru.android.inbbbox.localrepository.SettingsPrefsRepository;
import co.netguru.android.inbbbox.localrepository.TokenPrefsRepository;
import co.netguru.android.inbbbox.localrepository.UserPrefsRepository;
import rx.Completable;

public class LogoutController {


    private TokenPrefsRepository tokenPrefsRepository;
    private UserPrefsRepository userPrefsRepository;
    private SettingsPrefsRepository settingsPrefsRepository;

    @Inject
    public LogoutController(TokenPrefsRepository tokenPrefsRepository,
                            UserPrefsRepository userPrefsRepository,
                            SettingsPrefsRepository settingsPrefsRepository) {

        this.tokenPrefsRepository = tokenPrefsRepository;
        this.userPrefsRepository = userPrefsRepository;
        this.settingsPrefsRepository = settingsPrefsRepository;
    }

    public Completable performLogout() {
        return Completable.merge(tokenPrefsRepository.clear(),
                userPrefsRepository.clear(),
                settingsPrefsRepository.clear());
    }
}
