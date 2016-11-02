package co.netguru.android.inbbbox.feature.shots;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.feature.errorhandling.ErrorMessageParser;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

public class ShotsPresenter extends MvpNullObjectBasePresenter<ShotsContract.View>
        implements ShotsContract.Presenter {

    private final ShotsProvider shotsProvider;
    private final ErrorMessageParser errorMessageParser;
    private List<Shot> items;

    @Inject
    ShotsPresenter(ShotsProvider shotsProvider, ErrorMessageParser errorMessageParser) {

        this.shotsProvider = shotsProvider;
        this.errorMessageParser = errorMessageParser;
    }

    @Override
    public void attachView(ShotsContract.View view) {
        super.attachView(view);
        loadShotsData();
    }

    private void loadShotsData() {
        shotsProvider.getShots()
                .compose(androidIO())
                .subscribe(this::showRetrievedItems,
                        this::handleException);
    }

    private void showRetrievedItems(List<Shot> shotsList) {
        Timber.d("Shots received!");
        this.items = shotsList;
        getView().showItems(shotsList);
    }

    private void handleException(Throwable exception) {
        Timber.e(exception, "Shots item receiving exception ");
        getView().showError(errorMessageParser.getError(exception));
    }
}
