package co.netguru.android.inbbbox.model.localrepository;

public class Settings {
    private final StreamSourceSettings streamSourceSettings;
    private final NotificationSettings notificationSettings;
    private final CustomizationSettings customizationSettings;

    public Settings(StreamSourceSettings streamSourceSettings, NotificationSettings notificationSettings,
                    CustomizationSettings customizationSettings) {
        this.streamSourceSettings = streamSourceSettings;
        this.notificationSettings = notificationSettings;
        this.customizationSettings = customizationSettings;
    }

    public StreamSourceSettings getStreamSourceSettings() {
        return streamSourceSettings;
    }

    public NotificationSettings getNotificationSettings() {
        return notificationSettings;
    }

    public CustomizationSettings getCustomizationSettings() {
        return customizationSettings;
    }
}

