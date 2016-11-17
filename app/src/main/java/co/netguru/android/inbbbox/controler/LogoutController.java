package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;

import co.netguru.android.inbbbox.localrepository.SettingsPrefsRepository;
import co.netguru.android.inbbbox.localrepository.TokenPrefsRepository;
import co.netguru.android.inbbbox.localrepository.UserPrefsRepository;
import rx.Completable;

import static co.netguru.android.inbbbox.utils.RxTransformerUtils.applyCompletableIoSchedulers;

public class LogoutController {


    private TokenPrefsRepository tokenPrefsRepository;
    private UserPrefsRepository userPrefsRepository;
    private SettingsPrefsRepository settingsPrefsRepository;
    private CacheController cacheController;

    @Inject
    public LogoutController(TokenPrefsRepository tokenPrefsRepository,
                            UserPrefsRepository userPrefsRepository,
                            SettingsPrefsRepository settingsPrefsRepository,
                            CacheController cacheController) {

        this.tokenPrefsRepository = tokenPrefsRepository;
        this.userPrefsRepository = userPrefsRepository;
        this.settingsPrefsRepository = settingsPrefsRepository;
        this.cacheController = cacheController;
    }

    public Completable performLogout() {
        return Completable.merge(tokenPrefsRepository.clear(),
                userPrefsRepository.clear(),
                settingsPrefsRepository.clear(),
                cacheController.clearCache())
                .compose(applyCompletableIoSchedulers());
    }
}
