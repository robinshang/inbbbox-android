package co.netguru.android.inbbbox.data.settings.model;

public class CustomizationSettings {

    private boolean showDetails;
    private boolean nightMode;

    public CustomizationSettings() {
        //no-op
    }

    public CustomizationSettings(boolean showDetails, boolean nightMode) {
        this.showDetails = showDetails;
        this.nightMode = nightMode;
    }

    public boolean isShowDetails() {
        return showDetails;
    }

    public boolean isNightMode() {
        return nightMode;
    }
}
