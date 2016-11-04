package co.netguru.android.inbbbox.feature.shots;

import javax.inject.Inject;

import co.netguru.android.inbbbox.models.FilteredShotsParams;
import co.netguru.android.inbbbox.models.StreamSourceSettings;
import co.netguru.android.inbbbox.utils.Constants;
import co.netguru.android.inbbbox.utils.LocalTimeFormatter;

public class ShotsParamsFactory {

    private final LocalTimeFormatter dateFormatter;

    @Inject
    ShotsParamsFactory(LocalTimeFormatter dateFormatter) {

        this.dateFormatter = dateFormatter;
    }

    public FilteredShotsParams getShotsParams(StreamSourceSettings streamSourceSettings) {
        FilteredShotsParams.Builder builder = FilteredShotsParams.newBuilder();

        boolean wasHandled = false;

        if (streamSourceSettings.isNewToday()) {
            builder.date(dateFormatter.getCurrentDate())
                    .list(Constants.API.LIST_PARAM_DEBUTS_PARAM);
            wasHandled = true;
        }

        if (streamSourceSettings.isPopularToday() && !wasHandled) {
            builder.date(dateFormatter.getCurrentDate());
            wasHandled = true;
        }

        if (streamSourceSettings.isDebut() && !wasHandled) {
            builder.list(Constants.API.LIST_PARAM_DEBUTS_PARAM);
        }
        return builder.build();
    }

}
