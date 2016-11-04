package co.netguru.android.inbbbox.models;

import java.io.Serializable;

public class Settings implements Serializable{
    private final StreamSourceSettings streamSourceSettings;
    private final NotificationSettings notificationSettings;
    private final CustomizationSettings customizationSettings;

    public Settings() {
        this.streamSourceSettings = new StreamSourceSettings();
        this.notificationSettings = new NotificationSettings();
        this.customizationSettings = new CustomizationSettings();
    }

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

