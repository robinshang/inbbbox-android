package co.netguru.android.inbbbox.models;

public class CustomizationSettings{

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
