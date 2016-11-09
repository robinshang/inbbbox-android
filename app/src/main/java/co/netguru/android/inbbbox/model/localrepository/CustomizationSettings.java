package co.netguru.android.inbbbox.model.localrepository;

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
