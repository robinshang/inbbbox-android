package co.netguru.android.inbbbox.feature.shots;

import javax.inject.Inject;

import co.netguru.android.inbbbox.data.models.FilteredShotsParams;
import co.netguru.android.inbbbox.data.models.StreamSourceState;
import co.netguru.android.inbbbox.utils.Constants;
import co.netguru.android.inbbbox.utils.LocalTimeFormatter;

public class ShotsParamsFactory {

    private final LocalTimeFormatter dateFormatter;

    @Inject
    ShotsParamsFactory(LocalTimeFormatter dateFormatter) {

        this.dateFormatter = dateFormatter;
    }

    public FilteredShotsParams getShotsParams(StreamSourceState streamSourceState) {
        FilteredShotsParams.Builder builder = FilteredShotsParams.newBuilder();

        boolean wasHandled = false;

        if (streamSourceState.getNewTodayState()) {
            builder.date(dateFormatter.getCurrentDate())
                    .list(Constants.API.LIST_PARAM_DEBUTS_PARAM);
            wasHandled = true;
        }

        if (streamSourceState.getPopularTodayState() && !wasHandled) {
            builder.date(dateFormatter.getCurrentDate());
            wasHandled = true;
        }

        if (streamSourceState.getDebut() && !wasHandled) {
            builder.list(Constants.API.LIST_PARAM_DEBUTS_PARAM);
        }
        return builder.build();
    }

}
