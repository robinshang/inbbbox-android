package co.netguru.android.inbbbox.data.models;

import java.io.Serializable;

public class CustomizationSettings implements Serializable {

    private boolean showDetails;

    public CustomizationSettings() {

    }

    public CustomizationSettings(boolean showDetails) {
        this.showDetails = showDetails;
    }

    public boolean isShowDetails() {
        return showDetails;
    }
}
