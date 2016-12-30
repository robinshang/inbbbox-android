package co.netguru.android.inbbbox.feature.splash;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.inbbbox.common.error.ErrorController;
import co.netguru.android.inbbbox.data.dribbbleuser.user.UserController;
import co.netguru.android.inbbbox.data.session.controllers.TokenController;
import co.netguru.android.inbbbox.data.settings.SettingsController;
import co.netguru.android.inbbbox.data.settings.model.CustomizationSettings;
import rx.Single;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;
import static co.netguru.android.inbbbox.common.utils.RxTransformerUtil.applySingleIoSchedulers;

public class SplashPresenter extends MvpNullObjectBasePresenter<SplashContract.View>
        implements SplashContract.Presenter {

    private final TokenController tokenController;
    private final UserController userController;
    private final SettingsController settingsController;
    private final ErrorController errorController;
    private final CompositeSubscription compositeSubscription;

    @Inject
    SplashPresenter(TokenController tokenController, UserController userController,
                    SettingsController settingsController, ErrorController errorController) {
        this.tokenController = tokenController;
        this.userController = userController;
        this.settingsController = settingsController;
        this.errorController = errorController;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void attachView(SplashContract.View view) {
        super.attachView(view);
        checkToken();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        compositeSubscription.unsubscribe();
    }

    @Override
    public void initializeDefaultNightMode() {
        // TODO: 15.12.2016 Set local night mode depending on user configuration
        compositeSubscription.add(
                settingsController.getCustomizationSettings()
                        .map(CustomizationSettings::isNightMode)
                        .subscribe(getView()::setDefaultNightMode,
                                throwable -> handleError(throwable, "Error while getting night mode settings")));
    }

    @Override
    public void handleError(Throwable throwable, String errorText) {
        Timber.e(throwable, errorText);
        getView().showMessageOnServerError(errorController.getThrowableMessage(throwable));
        getView().showLoginScreen();
    }

    private void checkToken() {
        compositeSubscription.add(
                getTokenValidationSingle()
                        .compose(applySingleIoSchedulers())
                        .subscribe(this::handleTokenVerificationResult,
                                throwable -> handleError(throwable, "Error while token validation"))
        );
    }

    private Single<Boolean> getTokenValidationSingle() {
        return Single.zip(tokenController.isTokenValid(),
                userController.isGuestModeEnabled(),
                (isTokenValid, isGuestModeEnabled) -> isTokenValid && !isGuestModeEnabled);
    }

    private void handleTokenVerificationResult(Boolean isValid) {
        if (isValid) {
            Timber.d("Token valid");
            getCurrentUserInstance();
        } else {
            Timber.d("Token invalid");
            getView().showLoginScreen();
        }
    }

    private void getCurrentUserInstance() {
        compositeSubscription.add(
                userController.requestUser()
                        .compose(androidIO())
                        .subscribe(user -> handleLoggedUser(),
                                throwable -> handleError(throwable, "Error while requesting user"))
        );
    }

    private void handleLoggedUser() {
        getView().initializeOnlineUserMode();
        getView().showMainScreen();
    }
}